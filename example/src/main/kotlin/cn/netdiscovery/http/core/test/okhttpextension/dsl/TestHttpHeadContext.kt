package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpHeadContext
 * @author: Tony Shen
 * @date: 2021/11/19 11:12 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    httpClient.head{

        url {
            url = "/response-headers"
        }

        header {
            "key1" to "value1"
            "key2" to "value2"
            "key3" to "value3"
        }
    }.use {
        println(it)
    }
}