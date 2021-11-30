package cn.netdiscovery.http.core.test.okhttpextension.stomp

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.core.websocket.stomp.StompClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.stomp.Test
 * @author: Tony Shen
 * @date: 2021/11/29 6:54 下午
 * @version: V1.0 <描述当前版本功能>
 */
data class Event(val name:String)

data class EchoModel(val message:String)

fun main() {

    val stompClient = StompClient("ws://127.0.0.1:8080",httpClient)
    stompClient.connect()

    val result: EchoModel? = stompClient.send("/echo/message", EchoModel("hello world"), EchoModel::class.java)
    println("Got result: ${result}")
}