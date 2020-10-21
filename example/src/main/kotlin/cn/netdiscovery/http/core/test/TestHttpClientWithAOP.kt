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
val httpClientWithAOP by lazy {
    HttpClientBuilder()
            .baseUrl("http://localhost:8080")
            .addInterceptor(loggingInterceptor)
            .converter(GsonConverter())
            .jsonConverter(GlobalRequestJSONConverter::class)
            .addRequestProcessor { httpClient, builder ->
                println("request start")
                builder
            }
            .addResponseProcessor {
                println("response start")
            }
            .build()
}

val apiServiceWithAOP by lazy {
    TestAPIService(httpClientWithAOP)
}

fun main() {
    testPostWithModel(apiServiceWithAOP)
}