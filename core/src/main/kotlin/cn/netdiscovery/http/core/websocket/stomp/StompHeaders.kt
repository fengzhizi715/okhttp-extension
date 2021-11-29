package cn.netdiscovery.http.core.websocket.stomp

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.StompHeaders
 * @author: Tony Shen
 * @date: 2021/11/29 3:10 下午
 * @version: V1.0 <描述当前版本功能>
 */
class StompHeaders {

    private val headers: MutableMap<String, String> = mutableMapOf()

    fun add(key: String, value: String) {
        headers[key] = value
    }

    private fun getHeader(key: String): String? {
        return headers[key]
    }

    @Throws(NoSuchElementException::class)
    fun getDestination(): String {
        return getHeader("destination") ?: throw NoSuchElementException("Destination not found")
    }

    override fun toString(): String {
        return headers.toString()
    }
}