package cn.netdiscovery.http.core.dsl.context

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.CookieContext
 * @author: Tony Shen
 * @date: 2021/11/15 12:35 上午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
class CookieContext {
    private val cookies: MutableList<Pair<String, Any>> = mutableListOf()

    infix fun String.to(v: Any) {
        cookies += Pair(this, v)
    }

    internal fun collect(): String = cookies.joinToString(separator = "; ") { (k, v) -> "$k=$v" }
}