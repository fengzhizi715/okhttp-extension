package cn.netdiscovery.http.core.test.okhttpextension.coroutines

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.coroutines.asyncGet
import kotlinx.coroutines.runBlocking

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.coroutines.TestAsyncGet
 * @author: Tony Shen
 * @date: 2021/11/16 9:10 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    runBlocking {
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
        }.await().use {
            println(it)
        }
    }
}