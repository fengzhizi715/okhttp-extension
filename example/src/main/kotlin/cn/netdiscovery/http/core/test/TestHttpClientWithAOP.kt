package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
import cn.netdiscovery.http.core.test.converter.GsonConverter

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.TestHttpClientWithAOP
 * @author: Tony Shen
 * @date: 2020-10-21 11:38
 * @version: V1.0 <描述当前版本功能>
 */
val apiServiceWithAOP by lazy {
    TestAPIService(httpClientWithAOP)
}

fun main() {
    testPostWithModel(apiServiceWithAOP)
}