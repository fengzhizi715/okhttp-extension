package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.exception.RequestMethodException
import cn.netdiscovery.http.core.exception.UrlNotFoundException
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
        var httpMethod: HttpMethodName = HttpMethodName.GET) {

    fun setMethod(method: RequestMethod<*>) {
        httpMethod = when(method) {
            is GetMethod      -> HttpMethodName.GET
            is PostMethod     -> HttpMethodName.POST
            is JsonPostMethod -> HttpMethodName.POST
            is PutMethod      -> HttpMethodName.PUT
            is JsonPutMethod  -> HttpMethodName.PUT
            is DeleteMethod   -> HttpMethodName.DELETE
            is HeadMethod     -> HttpMethodName.HEAD
            is PatchMethod    -> HttpMethodName.PATCH
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
            apiUrl != null    -> apiUrl = url
            customUrl != null -> customUrl = url
            else              -> throw UrlNotFoundException()
        }
    }

    fun build(): Request.Builder {
        if (apiUrl != null)
            requestBuilder.url("$baseUrl$apiUrl")
        else if (customUrl != null)
            requestBuilder.url(customUrl!!)

        when(httpMethod) {
            HttpMethodName.GET    -> requestBuilder.get()
            HttpMethodName.POST   -> {
                requestBody?.let {
                    requestBuilder.post(it)
                }?: throw RequestMethodException("Can't send post without body")
            }
            HttpMethodName.PUT    -> {
                requestBody?.let {
                    requestBuilder.put(it)
                }?: throw RequestMethodException("Can't send put without body")
            }
            HttpMethodName.DELETE -> requestBuilder.delete()
            HttpMethodName.HEAD   -> requestBuilder.head()
            HttpMethodName.PATCH  -> {
                requestBody?.let {
                    requestBuilder.patch(it)
                }?: throw RequestMethodException("Can't send patch without body")
            }
        }

        return requestBuilder
    }
}