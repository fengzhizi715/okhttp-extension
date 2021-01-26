package cn.netdiscovery.http.core.response

import cn.netdiscovery.http.core.utils.extension.stringBody
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.response.ResponseMapper
 * @author: Tony Shen
 * @date: 2020-10-05 01:34
 * @version: V1.0 Response 转换的接口
 */
interface ResponseMapper<out T> {

    fun map(response: Response): T
}

/**
 * 默认的 StringResponseMapper
 */
class StringResponseMapper : ResponseMapper<String> {

    override fun map(response: Response) = response.stringBody() ?: ""
}

/**
 * 默认的 EmptyResponseMapper
 */
class EmptyResponseMapper : ResponseMapper<Response> {

    override fun map(response: Response) = response
}