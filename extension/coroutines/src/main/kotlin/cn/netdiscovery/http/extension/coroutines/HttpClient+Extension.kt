package cn.netdiscovery.http.extension.coroutines

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.dsl.context.HttpGetContext
import com.safframework.kotlin.coroutines.asyncInBackground
import kotlinx.coroutines.Deferred
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.coroutines.`HttpClient+Extension`
 * @author: Tony Shen
 * @date: 2021/11/16 8:55 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun HttpClient.asyncGet(init: HttpGetContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpGetContext().apply(init).buildRequest(baseUrl))
    }
}