package cn.netdiscovery.http.core.domain.response

import cn.netdiscovery.http.core.exception.RequestMethodException
import okhttp3.Call
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.response.ResponseConsumer
 * @author: Tony Shen
 * @date: 2020-10-03 20:41
 * @version: V1.0 <描述当前版本功能>
 */
data class ResponseConsumer<T>(
        val call: Call? = null,
        val response: Response? = null,
        var responses: MutableList<Response>? = null,
        val responseModel: T? = null,
        var responseModels: MutableList<T>? = null
) {

    @Suppress("UNCHECKED_CAST")
    fun getResult(): T {
        val result = when {
            call != null -> call
            response != null -> response
            responses != null && responses?.isNotEmpty() == true -> responses
            responseModel != null -> responseModel
            responseModels != null && responseModels?.isNotEmpty() == true -> responseModels
            else -> throw RequestMethodException("ResponseConsumer is empty")
        }

        return result as T
    }
}