package cn.netdiscovery.http.core.websocket

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.WSStatus
 * @author: Tony Shen
 * @date: 2021-07-13 17:55
 * @version: V1.0 <描述当前版本功能>
 */
enum class WSStatus {
    CONNECTING, // 连接中
    CONNECTED,  // 已连接
    DISCONNECT  // 断开连接
}