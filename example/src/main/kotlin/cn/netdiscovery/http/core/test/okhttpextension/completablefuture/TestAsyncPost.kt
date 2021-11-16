package cn.netdiscovery.http.core.test.okhttpextension.completablefuture

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.core.utils.extension.asyncPost

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.completablefuture.TestAsyncPost
 * @author: Tony Shen
 * @date: 2021/11/17 2:03 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

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
    }.get().use{
        println(it)
    }
}