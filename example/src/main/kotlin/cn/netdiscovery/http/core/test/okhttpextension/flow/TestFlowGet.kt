package cn.netdiscovery.http.core.test.okhttpextension.flow

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.coroutines.flowGet
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.flow.TestFlowGet
 * @author: Tony Shen
 * @date: 2021/11/17 1:49 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    runBlocking {
        httpClient.flowGet{

            url{
                url = "/response-headers-queries"

                "param1" to "value1"
                "param2" to "value2"
            }

            header {
                "key1" to "value1"
                "key2" to "value2"
            }
        }.collect {
            println(it)
        }
    }
}