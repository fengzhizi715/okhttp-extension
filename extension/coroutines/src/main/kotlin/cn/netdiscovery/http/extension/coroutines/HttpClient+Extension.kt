package cn.netdiscovery.http.extension.coroutines

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.dsl.context.*
import com.safframework.kotlin.coroutines.asyncInBackground
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.coroutines.`HttpClient+Extension`
 * @author: Tony Shen
 * @date: 2021/11/16 8:55 下午
 * @version: V1.0 <描述当前版本功能>
 */
typealias ResponseHandler<T> = (Response) -> T

suspend fun <T> HttpClient.request(request: Request, responseHandler: ResponseHandler<T>): T =
    suspendCancellableCoroutine { cont ->
        val call = this.okHttpClient().newCall(request)
        cont.invokeOnCancellation { call.cancel() }
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val result = runCatching { response.use(responseHandler) }
                cont.resumeWith(result)
            }

            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }
        })
    }

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

fun HttpClient.flowGet(init: HttpGetContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpGetContext().apply(init).buildRequest(this.getBaseUrl()))

fun HttpClient.flowPost(init: HttpPostContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpPostContext().apply(init).buildRequest(this.getBaseUrl()))

fun HttpClient.flowPut(init: HttpPutContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpPutContext().apply(init).buildRequest(this.getBaseUrl()))

fun HttpClient.flowDelete(init: HttpDeleteContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpDeleteContext().apply(init).buildRequest(this.getBaseUrl()))

fun HttpClient.flowHead(init: HttpHeadContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpHeadContext().apply(init).buildRequest(this.getBaseUrl()))

fun HttpClient.flowPatch(init: HttpPatchContext.() -> Unit): Flow<Response> = okHttpClient().flowCall(HttpPatchContext().apply(init).buildRequest(this.getBaseUrl()))
