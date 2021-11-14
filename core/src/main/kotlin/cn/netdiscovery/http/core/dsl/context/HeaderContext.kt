package cn.netdiscovery.http.core.dsl.context

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.HeaderContext
 * @author: Tony Shen
 * @date: 2021/11/15 12:35 上午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
class HeaderContext {
    private val map: MutableMap<String, Any> = mutableMapOf()

    infix fun String.to(v: Any) {
        map[this] = v
    }

    fun cookie(init: CookieContext.() -> Unit) {
        map["cookie"] = CookieContext().also(init).collect()
    }

    internal fun forEach(action: (k: String, v: Any) -> Unit) = map.forEach { (k, v) -> action(k, v) }
}