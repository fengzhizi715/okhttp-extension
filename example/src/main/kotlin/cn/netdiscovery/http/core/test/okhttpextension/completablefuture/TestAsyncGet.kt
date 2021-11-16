package cn.netdiscovery.http.core.test.okhttpextension.completablefuture

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.core.utils.extension.asyncGet

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.completablefuture.TestAsyncGet
 * @author: Tony Shen
 * @date: 2021/11/17 1:51 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    httpClient.asyncGet{

        url{
            url = "/response-headers-queries"

            "param1" to "value1"
            "param2" to "value2"
        }

        header {
            "key1" to "value1"
            "key2" to "value2"
        }
    }.get().use {
        println(it)
    }
}