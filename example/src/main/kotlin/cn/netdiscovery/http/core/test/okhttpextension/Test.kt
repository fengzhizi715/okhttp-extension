package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.test.httpClient
import java.io.File

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.Test
 * @author: Tony Shen
 * @date: 2021/11/12 11:52 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    val file = File("/Users/tony/Downloads/WechatIMG3.png")

//    httpClient
//        .get(customUrl = "https://baidu.com")
//        .use {
//            println(it)
//        }

    httpClient.upload(url = "/upload",name = "file", file = file)
        .use {
            println(it)
        }
}