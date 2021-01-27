package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
import cn.netdiscovery.http.core.test.converter.GsonConverter
import cn.netdiscovery.http.interceptor.LoggingInterceptor
import cn.netdiscovery.http.interceptor.log.LogManager
import cn.netdiscovery.http.interceptor.log.LogProxy

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.HttpClientConfig
 * @author: Tony Shen
 * @date: 2021-01-27 11:37
 * @version: V1.0 <描述当前版本功能>
 */
val loggingInterceptor by lazy {
    LogManager.logProxy(object : LogProxy {  // 必须要实现 LogProxy ，否则无法打印网络请求的 request 、response
        override fun e(tag: String, msg: String) {
        }

        override fun w(tag: String, msg: String) {
        }

        override fun i(tag: String, msg: String) {
            println("$tag:$msg")
        }

        override fun d(tag: String, msg: String) {
            println("$tag:$msg")
        }
    })

    LoggingInterceptor.Builder()
        .loggable(true) // TODO: 发布到生产环境需要改成false
        .request()
        .requestTag("Request")
        .response()
        .responseTag("Response")
//        .hideVerticalLine()// 隐藏竖线边框
        .build()
}

val httpClient: HttpClient by lazy {
    HttpClientBuilder()
        .baseUrl("http://localhost:8080")
        .addInterceptor(loggingInterceptor)
        .converter(GsonConverter())
        .jsonConverter(GlobalRequestJSONConverter::class)
        .build()
}

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

val httpClientWithUA by lazy {
    HttpClientBuilder()
        .baseUrl("http://localhost:8080")
        .addInterceptor(loggingInterceptor)
        .converter(GsonConverter())
        .jsonConverter(GlobalRequestJSONConverter::class)
        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
        .build()
}