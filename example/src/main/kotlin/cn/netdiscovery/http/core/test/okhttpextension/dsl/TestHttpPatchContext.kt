package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpPatchContext
 * @author: Tony Shen
 * @date: 2021/11/19 10:55 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {
    httpClient.patch{

        url {
            url = "/response-body"
        }

        header {
            "key1" to "value1"
            "key2" to "value2"
        }

        body("application/json") {
            string("content")
        }
    }.use {
        println(it)
    }
}