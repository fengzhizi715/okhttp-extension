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
val httpClientWithUA by lazy {
    HttpClientBuilder()
            .baseUrl("http://localhost:8080")
            .addInterceptor(loggingInterceptor)
            .converter(GsonConverter())
            .jsonConverter(GlobalRequestJSONConverter::class)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
            .build()
}

val apiServiceWithUA by lazy {
    TestAPIService(httpClientWithUA)
}

fun main() {
    testGetWithHeader(apiServiceWithUA)
}