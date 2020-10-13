package cn.netdiscovery.http.core.response.mapper

import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.response.ResponseMapper
 * @author: Tony Shen
 * @date: 2020-10-05 01:34
 * @version: V1.0 <描述当前版本功能>
 */
interface ResponseMapper<out T> {
    fun map(response: Response): T
}

class StringResponseMapper : ResponseMapper<String> {
    override fun map(response: Response): String {
        return response.body?.string() ?: ""
    }
}

class EmptyResponseMapper : ResponseMapper<Response> {
    override fun map(response: Response): Response {
        return response
    }
}