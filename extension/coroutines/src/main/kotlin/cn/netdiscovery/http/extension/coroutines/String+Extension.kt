package cn.netdiscovery.http.extension.coroutines

import com.safframework.kotlin.coroutines.asyncInBackground
import kotlinx.coroutines.Deferred
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.coroutines.`String+Extension`
 * @author: Tony Shen
 * @date: 2021/11/17 1:10 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun String.asyncGet(client: Call.Factory = OkHttpClient()): Deferred<Response> = asyncInBackground{
    client.suspendCall(Request.Builder().url(this@asyncGet).build())
}