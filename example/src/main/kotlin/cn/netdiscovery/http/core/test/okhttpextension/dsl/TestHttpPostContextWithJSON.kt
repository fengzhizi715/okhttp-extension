package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpPostContextWithJSON
 * @author: Tony Shen
 * @date: 2021/11/16 7:34 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {
    httpClient.post{

        url {
            url = "/response-body"
        }

        body("application/json") {
            json {
                "key1" to "value1"
                "key2" to "value2"
                "key3" to "value3"
            }
        }
    }.use {
        println(it)
    }
}