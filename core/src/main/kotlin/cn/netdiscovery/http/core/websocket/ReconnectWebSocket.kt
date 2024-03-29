package cn.netdiscovery.http.core.websocket

import okhttp3.*
import okio.ByteString
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.ReconnectWebSocket
 * @author: Tony Shen
 * @date: 2021-07-13 17:49
 * @version: V1.0 基于 OkHttp 的 WebSocket 实现的包装类，具有自动重新连接的功能
 */
typealias OnConnectStatusChangeListener = (status: WSStatus) -> Unit

class ReconnectWebSocketWrapper (
    private val okHttpClient: OkHttpClient,
    private val request: Request,
    private val listener: WebSocketListener,
    private val config:WSConfig = WSConfig()
) : WebSocket {

    private val isConnected = AtomicBoolean(false)

    private val isConnecting = AtomicBoolean(false)

    private val isClosed = AtomicBoolean(false)

    /**
     * websocket 的连接状态
     */
    private val status: WSStatus
        get() = if (isConnected.get()) WSStatus.CONNECTED else if (isConnecting.get()) WSStatus.CONNECTING else WSStatus.DISCONNECT

    /**
     * WSStatus 变化的监听
     */
    var onConnectStatusChangeListener: OnConnectStatusChangeListener? = null

    /**
     * 重连次数
     */
    private val reconnectAttemptCount = AtomicInteger(0)

    private var timer: Timer? = null

    private val webSocketListener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            isConnected.compareAndSet(true, false)
            onConnectStatusChangeListener?.invoke(status)
            reconnectAttemptCount.incrementAndGet()
            doReconnect()
            listener.onClosed(webSocket, code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            listener.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isConnected.compareAndSet(true, false)
            onConnectStatusChangeListener?.invoke(status)
            reconnectAttemptCount.incrementAndGet()
            doReconnect()
            listener.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listener.onMessage(webSocket, text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            listener.onMessage(webSocket, bytes)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            isConnected.compareAndSet(false, true)
            isConnecting.compareAndSet(true, false)

            onConnectStatusChangeListener?.invoke(status)

            synchronized(this) {
                timer?.cancel()
                timer = null
            }
            reconnectAttemptCount.set(0)
            listener.onOpen(webSocket, response)
        }
    }

    private var webSocket: WebSocket

    init {
        onConnectStatusChangeListener?.invoke(status)
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    private fun doReconnect() {
        if (!config.isAllowReconnect) {
            return
        }

        if (isConnected.get() || isConnecting.get()) {
            return
        }

        if (isClosed.get()) {
            return
        }

        isConnecting.compareAndSet(false, true)

        onConnectStatusChangeListener?.invoke(status)

        synchronized(this) {
            if (timer == null) {
                timer = Timer()
            }
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {

                    if (reconnectAttemptCount.get() <= config.reconnectCount) {
                        webSocket.cancel()
                        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
                    }
                }
            }, config.reconnectDelay, config.reconnectInterval)
        }
    }

    override fun cancel() {
        isConnected.compareAndSet(true, false)
        onConnectStatusChangeListener?.invoke(status)
        timer?.cancel()
        timer = null
        webSocket.cancel()
    }

    override fun close(code: Int, reason: String?): Boolean {
        isClosed.compareAndSet(false,true)
        return webSocket.close(code, reason)
    }

    override fun queueSize(): Long = webSocket.queueSize()

    override fun request(): Request = webSocket.request()

    override fun send(text: String): Boolean = webSocket.send(text)

    override fun send(bytes: ByteString): Boolean = webSocket.send(bytes)
}