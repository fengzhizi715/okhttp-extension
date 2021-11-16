package cn.netdiscovery.http.extension.coroutines

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.dsl.context.*
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

fun HttpClient.asyncPost(init: HttpPostContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpPostContext().apply(init).buildRequest(baseUrl))
    }
}

fun HttpClient.asyncPut(init:  HttpPutContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpPutContext().apply(init).buildRequest(baseUrl))
    }
}

fun HttpClient.asyncDelete(init:  HttpDeleteContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpDeleteContext().apply(init).buildRequest(baseUrl))
    }
}

fun HttpClient.asyncHead(init: HttpHeadContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpHeadContext().apply(init).buildRequest(baseUrl))
    }
}

fun HttpClient.asyncPatch(init: HttpPatchContext.() -> Unit): Deferred<Response> {
    val baseUrl = this.getBaseUrl()
    val okHttpClient = this.okHttpClient()

    return asyncInBackground{
        okHttpClient.suspendCall(HttpPatchContext().apply(init).buildRequest(baseUrl))
    }
}