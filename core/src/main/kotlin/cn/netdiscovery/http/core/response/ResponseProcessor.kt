package cn.netdiscovery.http.core.response

import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.response.ResponseProcessor
 * @author: Tony Shen
 * @date: 2020-10-03 01:55
 * @version: V1.0 <描述当前版本功能>
 */
interface ResponseProcessor {

    fun process(response: Response)
}