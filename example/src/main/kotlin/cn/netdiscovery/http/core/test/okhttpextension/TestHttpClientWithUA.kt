package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.test.httpClientWithUA
import cn.netdiscovery.http.core.test.okhttpextension.ApiService

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.TestHttpClientWithUA
 * @author: Tony Shen
 * @date: 2020-10-22 16:55
 * @version: V1.0 <描述当前版本功能>
 */
val apiServiceWithUA by lazy {
    ApiService(httpClientWithUA)
}

fun main() {
    testGetWithHeader(apiServiceWithUA)
}