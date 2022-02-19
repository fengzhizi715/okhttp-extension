package cn.netdiscovery.http.core.websocket

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.WSConfig
 * @author: Tony Shen
 * @date: 2021-07-14 14:17
 * @version: V1.0 <描述当前版本功能>
 */
data class WSConfig(
    val isAllowReconnect: Boolean = true,
    val reconnectDelay: Long = 0,
    val reconnectCount: Int = Int.MAX_VALUE,
    val reconnectInterval: Long = 5000
)