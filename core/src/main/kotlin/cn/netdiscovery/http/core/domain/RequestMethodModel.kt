package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.exception.RequestMethodException
import cn.netdiscovery.http.core.exception.UrlNotFoundException
import cn.netdiscovery.http.core.method.*
import okhttp3.Request
import okhttp3.RequestBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.RequestMethodModel
 * @author: Tony Shen
 * @date: 2020-10-09 00:28
 * @version: V1.0 <描述当前版本功能>
 */
data class RequestMethodModel(
        val requestBuilder: Request.Builder,
        var requestBody: RequestBody? = null,
        val baseUrl: String = "",
        var apiUrl: String? = null,
        var customUrl: String? = null,
        var requestMethod: RequestMethodName = RequestMethodName.GET) {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun build(): Request.Builder {
        if (apiUrl != null)
            requestBuilder.url("$baseUrl$apiUrl")
        else if (customUrl != null)
            requestBuilder.url(customUrl!!)

        when(requestMethod) {
            RequestMethodName.GET -> requestBuilder.get()
            RequestMethodName.POST -> {
                requestBody ?: throw RequestMethodException("Can't send post without body")
                requestBuilder.post(requestBody!!)
            }
            RequestMethodName.PUT -> {
                requestBody ?: throw RequestMethodException("Can't send put without body")
                requestBuilder.put(requestBody!!)
            }
            RequestMethodName.DELETE -> requestBuilder.delete()
        }

        return requestBuilder
    }

    fun setMethod(method: RequestMethod<*>) {
        requestMethod = when(method) {
            is GetMethod      -> RequestMethodName.GET
            is PostMethod     -> RequestMethodName.POST
            is JsonPostMethod -> RequestMethodName.POST
            is PutMethod      -> RequestMethodName.PUT
            is JsonPutMethod  -> RequestMethodName.PUT
            is DeleteMethod   -> RequestMethodName.DELETE
            else              -> throw IllegalStateException()
        }
    }

    fun getUrl(): String {
        return if (apiUrl != null)
            apiUrl!!
        else
            customUrl ?: throw UrlNotFoundException()
    }

    fun setUrl(url: String) {
        when {
            apiUrl != null -> apiUrl = url
            customUrl != null -> customUrl = url
            else -> throw UrlNotFoundException()
        }
    }
}

enum class RequestMethodName {
    GET, POST, PUT, DELETE
}