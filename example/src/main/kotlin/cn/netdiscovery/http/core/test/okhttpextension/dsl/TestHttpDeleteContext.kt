package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpDeleteContext
 * @author: Tony Shen
 * @date: 2021/11/16 7:11 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    httpClient.delete{

        url {
            url = "/users/tony"
        }
    }.use {
        println(it)
    }
}