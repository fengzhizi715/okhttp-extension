package cn.netdiscovery.http.core.utils.extension

import okhttp3.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`OkHttpClient+Extension`
 * @author: Tony Shen
 * @date: 2020-10-09 21:30
 * @version: V1.0 <描述当前版本功能>
 */
fun OkHttpClient.newCall(block:()->Request):Call = this.newCall(block.invoke())

fun OkHttpClient.newWebSocket(block:()->Request,listener: WebSocketListener):WebSocket = this.newWebSocket(block.invoke(),listener)