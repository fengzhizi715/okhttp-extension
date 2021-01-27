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
 *          cn.netdiscovery.http.core.test.Test
 * @author: Tony Shen
 * @date: 2020-10-11 01:20
 * @version: V1.0 <描述当前版本功能>
 */
val apiService by lazy {
    TestAPIService(httpClient)
}

fun main() {
    testPostWithModel(apiService)
}