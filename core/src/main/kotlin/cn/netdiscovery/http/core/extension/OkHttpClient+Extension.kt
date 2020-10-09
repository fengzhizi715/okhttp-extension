package cn.netdiscovery.http.core.extension

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`OkHttpClient+Extension`
 * @author: Tony Shen
 * @date: 2020-10-09 21:30
 * @version: V1.0 <描述当前版本功能>
 */
fun OkHttpClient.newCall(block:()->Request):Call = newCall(block.invoke())