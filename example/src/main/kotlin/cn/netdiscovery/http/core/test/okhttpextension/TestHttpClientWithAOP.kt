package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.test.httpClientWithAOP

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.TestHttpClientWithAOP
 * @author: Tony Shen
 * @date: 2020-10-21 11:38
 * @version: V1.0 <描述当前版本功能>
 */
val apiServiceWithAOP by lazy {
    ApiService(httpClientWithAOP)
}

fun main() {
    testPostWithModel(apiServiceWithAOP)
}