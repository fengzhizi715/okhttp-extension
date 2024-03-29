package cn.netdiscovery.http.core.dsl.context

import cn.netdiscovery.http.core.domain.Params

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
    private val params: Params = Params.from(mutableMapOf())

    infix fun String.to(v: Any) {
        params.add(Pair(this,v.toString()))
    }

    fun cookie(init: CookieContext.() -> Unit) {
        params.add(Pair("cookie",CookieContext().also(init).collect()))
    }

    internal fun forEach(action: (k: String, v: Any) -> Unit) = params.forEach { (k, v) -> action(k, v) }
}