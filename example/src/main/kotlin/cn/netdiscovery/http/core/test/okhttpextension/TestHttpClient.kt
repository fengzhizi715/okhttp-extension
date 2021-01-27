package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.test.httpClient


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.Test
 * @author: Tony Shen
 * @date: 2020-10-11 01:20
 * @version: V1.0 <描述当前版本功能>
 */
val apiService by lazy {
    ApiService(httpClient)
}

fun main() {
    testPostWithModel(apiService)
}