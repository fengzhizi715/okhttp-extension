package cn.netdiscovery.http.core.test.okhttpextension.coroutines

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.extension.coroutines.request
import kotlinx.coroutines.runBlocking
import okhttp3.Request

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.coroutines.Test
 * @author: Tony Shen
 * @date: 2021/12/18 4:50 PM
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    runBlocking {
        val request = Request.Builder().url("https://baidu.com").build()

        httpClient.request(request) {
            println(it)
        }
    }
}