package cn.netdiscovery.http.core.websocket.stomp

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.Message
 * @author: Tony Shen
 * @date: 2021/11/29 3:23 下午
 * @version: V1.0 <描述当前版本功能>
 */
data class StompMessage(
    val command: StompCommand,
    val headers: StompHeaders,
    val payload: String? = null
)
