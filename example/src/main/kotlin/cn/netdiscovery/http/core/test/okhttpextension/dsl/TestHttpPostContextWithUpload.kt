package cn.netdiscovery.http.core.test.okhttpextension.dsl

import cn.netdiscovery.http.core.test.httpClient
import java.io.File

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.dsl.TestHttpPostContextWithUpload
 * @author: Tony Shen
 * @date: 2021/11/16 2:10 下午
 * @version: V1.0 <描述当前版本功能>
 */
val file = File("/Users/tony/Downloads/WechatIMG3.png")

fun main() {
    httpClient.post{

        url {
            url = "/upload"
        }

        multipartBody {
            +part("file", file.name) {
                file(file)
            }
        }
    }.use {
        println(it)
    }
}