package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpGetContext
 * @author: Tony Shen
 * @date: 2021/11/16 1:01 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    httpClient.get{

        url {
            url = "/response-headers-queries"

            "param1" to "value1"
            "param2" to "value2"
        }

        header {
            "key1" to "value1"
            "key2" to "value2"
        }
    }.use {
        println(it)
    }
}