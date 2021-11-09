package cn.netdiscovery.http.core.utils.extension

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.`String+Extension`
 * @author: Tony Shen
 * @date: 2021/11/9 5:26 下午
 * @version: V1.0 <描述当前版本功能>
 */

fun String.httpGet(client: Call.Factory = OkHttpClient()): Response = client.newCall(Request.Builder().url(this).build()).execute()