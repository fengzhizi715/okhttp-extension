package cn.netdiscovery.http.core.websocket.stomp

import cn.netdiscovery.http.core.exception.StompException
import cn.netdiscovery.http.core.serializer.SerializerManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.StompMessageUtils
 * @author: Tony Shen
 * @date: 2021/11/29 4:45 下午
 * @version: V1.0 <描述当前版本功能>
 */

private val END = "\u0000"
private val NEW_LINE = "\n"
private val DESTINATION = "destination"
private val ID = "id"
private val ACK = "ack"
private val RECEIPT = "receipt"
private val EMPTY_LINE = ""
private val DELIMITER = ":"

/**
 * It parses the current STOMP message and returns a [Message].
 * @return the wrapped STOMP message as [Message]
 */
@Throws(StompException::class)
fun parseStompMessage(stompMessage: String): StompMessage {
    val splitMessage = stompMessage.split(NEW_LINE.toRegex()).toTypedArray()

    if (splitMessage.isEmpty())
        throw StompException("Did not received any message")

    val command = splitMessage[0]
    val stompHeaders = StompHeaders()
    var body = ""

    var cursor = 1
    for (i in cursor until splitMessage.size) {
        // empty line
        if (splitMessage[i] == EMPTY_LINE) {
            cursor = i
            break
        } else {
            val header: List<String> = splitMessage[i].split(DELIMITER)
            stompHeaders.add(header[0], header[1])
        }
    }

    for (i in cursor until splitMessage.size) {
        body += splitMessage[i]
    }

    return if (body.isNotEmpty())
        StompMessage(StompCommand.valueOf(command), stompHeaders, body.replace(END,""))
    else
        StompMessage(StompCommand.valueOf(command), stompHeaders)
}

fun buildSubscribeMessage(destination: String, id: String): String {
    var headers = ""

    headers += buildHeader(StompCommand.SUBSCRIBE.name)
    headers += buildHeader(DESTINATION, destination)
    headers += buildHeader(ID, id)
    headers += buildHeader(ACK, "auto")
    headers += buildHeader(RECEIPT, "receipt_$destination")

    return "$headers$NEW_LINE$END"
}

fun buildUnsubscribeMessage(subscriptionId: String): String {
    var headers = ""

    headers += buildHeader(StompCommand.UNSUBSCRIBE.name)
    headers += buildHeader(ID, subscriptionId)

    return "$headers$NEW_LINE$END"
}

fun buildConnectMessage(): String {
    var headers = ""

    headers += buildHeader(StompCommand.CONNECT.name)
    headers += buildHeader("accept-version", "1.0,1.1,2.0")
    headers += buildHeader("host", "stomp.github.org")
    headers += buildHeader("heart-beat", "0,0")

    return "$headers$NEW_LINE$END"
}

fun <T> buildSendMessage(destination: String, payload: T?): String {
    val body = if (payload != null) SerializerManager.toJson(payload) else ""
    var headers = ""

    headers += buildHeader(StompCommand.SEND.name)
    headers += buildHeader(DESTINATION, destination)

    return "$headers$NEW_LINE$body$NEW_LINE$END"
}

private fun buildHeader(key: String, value: String? = null): String = if (value != null) "$key:$value$NEW_LINE" else "$key$NEW_LINE"
