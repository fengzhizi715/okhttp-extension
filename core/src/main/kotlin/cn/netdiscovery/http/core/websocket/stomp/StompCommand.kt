package cn.netdiscovery.http.core.websocket.stomp

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.StompCommand
 * @author: Tony Shen
 * @date: 2021/11/29 3:08 下午
 * @version: V1.0 <描述当前版本功能>
 */
enum class StompCommand {
    SUBSCRIBE,
    UNSUBSCRIBE,
    CONNECT,
    CONNECTED,
    RECEIPT,
    SEND,
    ERROR,
    MESSAGE
}
