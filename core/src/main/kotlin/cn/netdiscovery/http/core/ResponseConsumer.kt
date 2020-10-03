package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.exception.RequestMethodException
import okhttp3.Call
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.ResponseConsumer
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
        }!!

        /*// TODO change with findGeneric
        val returnable = *//*this.findGenerics().first()*//* this::class

		if (!(returnable parentOf result::class))
			throw TypeNotFoundException("Expected ${returnable.qualifiedName} actual ${result::class.qualifiedName}")*/

        return result as T
    }
}