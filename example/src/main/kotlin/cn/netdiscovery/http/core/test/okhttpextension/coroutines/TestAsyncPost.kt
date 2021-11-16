package cn.netdiscovery.http.core.test.okhttpextension.coroutines

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.coroutines.asyncPost
import kotlinx.coroutines.runBlocking

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.coroutines.TestAsyncPost
 * @author: Tony Shen
 * @date: 2021/11/16 9:41 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    runBlocking{
        httpClient.asyncPost{

            url {
                url = "/response-body"
            }

            header {
                "key1" to "value1"
                "key2" to "value2"
            }

            body("application/json") {
                json {
                    "key1" to "value1"
                    "key2" to "value2"
                    "key3" to "value3"
                }
            }
        }.await().use{
            println(it)
        }
    }
}