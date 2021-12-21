package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.response.StringResponseMapper
import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.rxjava3.asObservable

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpGetContextWithObservable
 * @author: Tony Shen
 * @date: 2021/12/22 1:12 AM
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    httpClient.get {

        url {
            url = "/response-headers-queries"

            "param1" to "value1"
            "param2" to "value2"
        }

        header {
            "key1" to "value1"
            "key2" to "value2"
        }

    }.asObservable(StringResponseMapper())
    .subscribe {
        println(it)
     }
}