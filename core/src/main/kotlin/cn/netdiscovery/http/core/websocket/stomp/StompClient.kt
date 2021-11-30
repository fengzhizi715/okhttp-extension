package cn.netdiscovery.http.core.websocket.stomp

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.exception.StompException
import cn.netdiscovery.http.core.websocket.ReconnectWebSocketWrapper
import cn.netdiscovery.http.core.websocket.WSConfig
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.BiConsumer

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.StompClient
 * @author: Tony Shen
 * @date: 2021/11/29 5:11 下午
 * @version: V1.0 <描述当前版本功能>
 */
class StompClient(
    private val url: String,
    private val httpClient: HttpClient,
    private val log: (String) -> Unit = ::println
) : AutoCloseable {

    /**
     * The unique identifier of this client. This allows more clients to connect to the same server.
     */
    private val clientKey = generateClientKey()

    /**
     * The websockets subscriptions open so far with this client, per topic.
     */
    private val subscriptions: ConcurrentHashMap<String, StompSubscription> = ConcurrentHashMap()

    /**
     * The websockets queues where the results are published and consumed, per topic.
     */
    private val queues: ConcurrentHashMap<String, BlockingQueue<Any>> = ConcurrentHashMap()

    /**
     * Lock to synchronize the initial connection of the websocket client.
     */
    private val LOCK = Object()

    /**
     * Boolean to track whether the client is connected.
     */
    private var isClientConnected = false

    /**
     * The websocket instance.
     */
    private lateinit var ws: ReconnectWebSocketWrapper

    /**
     * It opens a webSocket connection and connects to the STOMP endpoint.
     * @param onStompConnectionOpened handler for a successful STOMP endpoint connection
     * @param onWebSocketFailure handler for the webSocket connection failure due to an error reading from or writing to the network
     * @param onWebSocketClosed handler for the webSocket connection when both peers have indicated that no more messages
     * will be transmitted and the connection has been successfully released.
     */
    fun connect(
        onStompConnectionOpened: (() -> Unit)? = null,
        onWebSocketFailure: (() -> Unit)? = null,
        onWebSocketClosed: (() -> Unit)? = null
    ) {
        log("[Stomp client] Connecting to $url ...")

        ws = httpClient.websocket(customUrl = url, headers = Params.of("uuid" to clientKey),listener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                // open the stomp session
                webSocket.send(buildConnectMessage())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                try {
                    val message = parseStompMessage(text)
                    val payload = message.payload

                    when (message.command) {

                        StompCommand.CONNECTED -> {
                            log("[Stomp client] Connected to stomp session")
                            onStompConnectionOpened?.invoke()
                            emitClientConnected()
                        }
                        StompCommand.RECEIPT -> {
                            val destination = message.headers.getDestination()
                            log("[Stomp client] Subscribed to topic $destination")

                            val subscription =
                                subscriptions[destination] ?: throw NoSuchElementException("Topic not found")
                            subscription.emitSubscription()
                        }
                        StompCommand.ERROR -> {
                            log("[Stomp client] STOMP Session Error: $payload")
                            // clean-up client resources because the server closed the connection
                            close()
                            onWebSocketFailure?.invoke()
                        }
                        StompCommand.MESSAGE -> {
                            val destination = message.headers.getDestination()
                            log("[Stomp client] Received message from topic $destination")
                            handleStompDestinationResult(payload, destination)
                        }
                        else -> log("Got an unknown message")
                    }

                } catch (e: Exception) {
                    log("[Stomp client] Got an exception while handling message")
                    e.printStackTrace()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                log("[Stomp client] WebSocket Session error")
                t.printStackTrace()

                close()
                onWebSocketFailure?.invoke()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                log("[Stomp client] WebSocket session closed")
                onWebSocketClosed?.invoke()
            }
        }, wsConfig = WSConfig())

        awaitClientConnection()
    }

    /**
     * Emits that the client is connected.
     */
    private fun emitClientConnected() {
        synchronized(LOCK) {
            LOCK.notify()
        }
    }

    /**
     * Awaits if necessary until the websocket client is connected.
     */
    private fun awaitClientConnection() {
        synchronized(LOCK) {
            if (!isClientConnected) try {
                LOCK.wait()
                isClientConnected = true
            } catch (e: InterruptedException) {
                log("[Stomp client] Interrupted while waiting for subscription")
                e.printStackTrace()
            }
        }
    }

    /**
     * Returns the key of this client instance. Each instance has a different key.
     */
    fun getClientKey(): String = this.clientKey

    /**
     * Subscribes to a topic providing a {@link BiConsumer} handler to handle the result published by the topic.
     * The subscription is recycled and the method awaits for the subscription to complete.
     * @param topic the topic
     * @param resultType the result type class
     * @param handler the handler to consume with the result and/or error
     */
    fun <T> subscribeToTopic(
        topic: String,
        resultType: Class<T>,
        handler: BiConsumer<T?, ErrorModel?>
    ) {

        val subscription = subscriptions.computeIfAbsent(topic) {

            val resultHandler = object : MessageHandler<T>(resultType) {

                override fun deliverResult(result: String) {
                    try {
                        handler.accept(toModel(result), null)
                    } catch (e: Exception) {
                        deliverError(ErrorModel(e.message ?: "Got a deserialization error", StompException::class.java.name))
                    }
                }

                override fun deliverError(errorModel: ErrorModel) {
                    handler.accept(null, errorModel)
                }

                override fun deliverNothing() {
                    handler.accept(null, null)
                }
            }

            subscribeInternal(it, resultHandler)
        }

        subscription.awaitSubscription()
    }

    /**
     * It sends a payload to a previous subscribed topic. The method {@link #subscribeToTopic(String, Class, BiConsumer)}}
     * is used to subscribe to a topic.
     *
     * @param topic   the topic
     * @param payload the payload
     */
    fun <T> sendToTopic(
        topic: String,
        payload: T
    ) {
        log("[Stomp client] Sending to ${topic}")
        ws.send(buildSendMessage(topic, payload))
    }

    /**
     * It sends a message payload to the standard "user" topic destination by performing an initial subscription
     * and then it waits for the result. The subscription is recycled.
     *
     * @param topic      the topic
     * @param resultType the result type
     */
    fun <T> send(
        topicDestination: String,
        resultType: Class<T>,
    ): T? = send(topicDestination,null, resultType)

    /**
     * It sends a message payload to the standard "user" topic destination by performing an initial subscription
     * and then it waits for the result. The subscription is recycled.
     *
     * @param <T>        the type of the expected result
     * @param <P>        the type of the payload
     * @param topicDestination      the topic
     * @param payload    the payload
     * @param resultType the result type
     *
     */
    fun <T, P> send(
        topicDestination: String,
        payload: P,
        resultType: Class<T>
    ): T? {
        log("[Stomp client] Subscribing to ${topicDestination}")

        val resultTopic = "/user/$clientKey$topicDestination"
        val result: Any

        val queue = queues.computeIfAbsent(topicDestination) { LinkedBlockingQueue(1) }
        synchronized(queue) {
            subscribe(resultTopic, resultType, queue)

            log("[Stomp client] Sending payload to $topicDestination")
            ws.send(buildSendMessage(topicDestination, payload))
            result = queue.take()
        }

        return when (result) {
            is Nothing -> null
            is ErrorModel -> throw Exception (
                result.toString()
            )
            else -> result as T
        }
    }

    /**
     * Subscribes to a topic.
     *
     * @param topic      the topic
     * @param resultType the result type
     * @param queue      the queue
     * @param <T>        the result type
     */
    private fun <T> subscribe(
        topic: String,
        resultType: Class<T>,
        queue: BlockingQueue<Any>
    ) {

        val subscription = subscriptions.computeIfAbsent(topic) {

            val resultHandler = object : MessageHandler<T>(resultType) {

                override fun deliverResult(result: String) {
                    try {
                        deliverInternal(toModel(result))
                    } catch (e: Exception) {
                        deliverError(ErrorModel(e.message ?: "Got a deserialization error", StompException::class.java.name))
                    }
                }

                override fun deliverError(errorModel: ErrorModel) {
                    deliverInternal(errorModel)
                }

                override fun deliverNothing() {
                    deliverInternal(Nothing())
                }

                private fun deliverInternal(result: Any) {
                    try {
                        queue.put(result)
                    } catch (e: java.lang.Exception) {
                        log("[Stomp client] Queue put error")
                        e.printStackTrace()
                    }
                }
            }

            subscribeInternal(it, resultHandler)
        }
        subscription.awaitSubscription()
    }


    /**
     * It handles a STOMP result message of a destination.
     *
     * @param result      the result
     * @param destination the destination
     */
    private fun handleStompDestinationResult(result: String?, destination: String) {
        val subscription = subscriptions[destination]
        subscription?.let {
            val resultHandler = it.messageHandler
            if (resultHandler.clazz == Unit::class.java || result == null || result == "null")
                resultHandler.deliverNothing()
            else
                resultHandler.deliverResult(result)
        }
    }

    /**
     * Internal method to subscribe to a topic. The subscription is recycled.
     *
     * @param topic   the topic
     * @param handler the result handler of the topic
     * @return the subscription
     */
    private fun subscribeInternal(topic: String, handler: MessageHandler<*>): StompSubscription {
        val subscriptionId = "" + (subscriptions.size + 1)
        val subscription = StompSubscription(topic, subscriptionId, handler)
        ws.send(buildSubscribeMessage(subscription.topic, subscription.subscriptionId))

        return subscription
    }

    /**
     * It unsubscribes from a topic.
     *
     * @param subscription the subscription
     */
    private fun unsubscribeFrom(subscription: StompSubscription) {
        log("[Stomp client] Unsubscribing from ${subscription.topic}")
        ws.send(buildUnsubscribeMessage(subscription.subscriptionId));
    }

    /**
     * Clears the subscriptions and closes the webSocket connection.
     */
    override fun close() {
        subscriptions.values.forEach { unsubscribeFrom(it) }
        subscriptions.clear()
        ws.close(1000, null)
    }

    /**
     * Generates an UUID for this webSocket client.
     */
    private fun generateClientKey(): String = try {
        val salt = MessageDigest.getInstance("SHA-256")
        salt.update(UUID.randomUUID().toString().toByteArray(StandardCharsets.UTF_8))
        bytesToHex(salt.digest())
    } catch (e: java.lang.Exception) {
        UUID.randomUUID().toString()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val byteArray = "0123456789abcdef".toByteArray()
        val hexChars = ByteArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = byteArray[v ushr 4]
            hexChars[j * 2 + 1] = byteArray[v and 0x0F]
        }
        return String(hexChars, StandardCharsets.UTF_8)
    }

    /**
     * Special object to wrap a NOP.
     */
    inner class Nothing
}