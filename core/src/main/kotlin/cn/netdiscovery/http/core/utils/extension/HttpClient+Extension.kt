package cn.netdiscovery.http.core.utils.extension

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.dsl.context.*
import okhttp3.Response
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.`HttpClient+Extension`
 * @author: Tony Shen
 * @date: 2021/11/17 1:27 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun HttpClient.asyncGet(init: HttpGetContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall{
    HttpGetContext().apply(init).buildRequest(this.getBaseUrl())
}

fun HttpClient.asyncPost(init: HttpPostContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall {
    HttpPostContext().apply(init).buildRequest(this.getBaseUrl())
}

fun HttpClient.asyncPut(init:  HttpPutContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall {
    HttpPutContext().apply(init).buildRequest(this.getBaseUrl())
}

fun HttpClient.asyncDelete(init:  HttpDeleteContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall{
    HttpDeleteContext().apply(init).buildRequest(this.getBaseUrl())
}

fun HttpClient.asyncHead(init: HttpHeadContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall {
    HttpHeadContext().apply(init).buildRequest(this.getBaseUrl())
}

fun HttpClient.asyncPatch(init: HttpPatchContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall{
    HttpPatchContext().apply(init).buildRequest(this.getBaseUrl())
}