package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.exception.RequestMethodException
import okhttp3.Call
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.ResponseConsumer
 * @author: Tony Shen
 * @date: 2020-10-03 20:41
 * @version: V1.0 <描述当前版本功能>
 */
data class ResponseConsumer<T>(
        val call: Call? = null,
        val response: Response? = null,
        val responseModel: T? = null
) {

    @Suppress("UNCHECKED_CAST")
    fun getResult(): T {
        val result = when {
            call != null                    -> call
            response != null                -> response
            responseModel != null           -> responseModel
            else                            -> throw RequestMethodException("ResponseConsumer is empty")
        }

        return result as T
    }
}