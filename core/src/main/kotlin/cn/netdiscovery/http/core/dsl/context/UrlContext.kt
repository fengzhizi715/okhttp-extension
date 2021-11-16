package cn.netdiscovery.http.core.dsl.context

import cn.netdiscovery.http.core.domain.Params

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.UrlContext
 * @author: Tony Shen
 * @date: 2021/11/15 2:44 下午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
class UrlContext {
    private val params:Params = Params.from(mutableMapOf())

    var url:String?=""
    var customUrl:String?=null

    infix fun String.to(v: Any) {
        params.add(Pair(this,v.toString()))
    }

    fun getParams() = params
}