package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
import cn.netdiscovery.http.core.test.converter.GsonConverter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.TestHttpClientWithUA
 * @author: Tony Shen
 * @date: 2020-10-22 16:55
 * @version: V1.0 <描述当前版本功能>
 */
val apiServiceWithUA by lazy {
    TestAPIService(httpClientWithUA)
}

fun main() {
    testGetWithHeader(apiServiceWithUA)
}