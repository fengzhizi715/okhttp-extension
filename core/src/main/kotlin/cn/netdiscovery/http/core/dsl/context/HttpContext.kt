package cn.netdiscovery.http.core.dsl.context

import cn.netdiscovery.http.core.domain.HttpMethodName
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.HttpContext
 * @author: Tony Shen
 * @date: 2021/11/12 6:00 下午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
abstract class AbstractHttpContext(private val method: HttpMethodName = HttpMethodName.GET) : HttpContext {

    private val urlContext = UrlContext()
    private val headerContext = HeaderContext()

    override fun url(init: UrlContext.() -> Unit) {
        urlContext.init()
    }

    override fun header(init: HeaderContext.() -> Unit) {
        headerContext.init()
    }

    override fun buildRequest(baseUrl:String): Request = with(Request.Builder()) {

        val query = urlContext.getParams()?.joinToString("&") { "${it.first}=${it.second}" }

        val url = if (query.isNotEmpty()) {
            val base = urlContext.customUrl ?: "$baseUrl${urlContext.url}"
            "$base?$query"
        } else {
            urlContext.customUrl ?: "$baseUrl${urlContext.url}"
        }

        println("url:$url")

        url(url)

        headers(Headers.Builder().apply {
            headerContext.forEach { k, v ->
                add(k, v.toString())
            }
        }.build())

        when (method) {
            HttpMethodName.POST, HttpMethodName.PUT, HttpMethodName.PATCH, HttpMethodName.DELETE -> method(method.name, makeBody())
            HttpMethodName.HEAD -> head()
            HttpMethodName.GET  -> get()
        }

        return build()
    }

    abstract fun makeBody(): RequestBody?
}

class HttpGetContext : AbstractHttpContext() {

    override fun makeBody(): RequestBody? = null
}

internal interface HttpContext {

    fun url(init:UrlContext.() -> Unit)

    fun header(init: HeaderContext.() -> Unit)

    fun buildRequest(baseUrl:String): Request

}

@DslMarker
annotation class HttpDslMarker