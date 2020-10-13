package cn.netdiscovery.http.core.request

import cn.netdiscovery.http.core.HttpClient
import okhttp3.Request

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.RequestProcessor
 * @author: Tony Shen
 * @date: 2020-10-03 01:52
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestProcessor {

    fun process(httpClient: HttpClient, requestBuilder: Request.Builder): Request.Builder
}