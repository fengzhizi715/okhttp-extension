package cn.netdiscovery.http.core.test.okhttpextension.flow

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.coroutines.flowPost
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.flow.TestFlowPost
 * @author: Tony Shen
 * @date: 2021/11/17 2:06 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    runBlocking{
        httpClient.flowPost{

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
        }.collect{
            println(it)
        }
    }
}