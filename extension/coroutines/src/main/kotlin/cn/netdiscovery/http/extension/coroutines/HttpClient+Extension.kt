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
fun HttpClient.asyncGet(init: HttpGetContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncGet.okHttpClient().suspendCall(HttpGetContext().apply(init).buildRequest(this@asyncGet.getBaseUrl()))
}

fun HttpClient.asyncPost(init: HttpPostContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncPost.okHttpClient().suspendCall(HttpPostContext().apply(init).buildRequest(this@asyncPost.getBaseUrl()))
}

fun HttpClient.asyncPut(init:  HttpPutContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncPut.okHttpClient().suspendCall(HttpPutContext().apply(init).buildRequest(this@asyncPut.getBaseUrl()))
}

fun HttpClient.asyncDelete(init:  HttpDeleteContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncDelete.okHttpClient().suspendCall(HttpDeleteContext().apply(init).buildRequest(this@asyncDelete.getBaseUrl()))
}

fun HttpClient.asyncHead(init: HttpHeadContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncHead.okHttpClient().suspendCall(HttpHeadContext().apply(init).buildRequest(this@asyncHead.getBaseUrl()))
}

fun HttpClient.asyncPatch(init: HttpPatchContext.() -> Unit): Deferred<Response> = asyncInBackground{
    this@asyncPatch.okHttpClient().suspendCall(HttpPatchContext().apply(init).buildRequest(this@asyncPatch.getBaseUrl()))
}