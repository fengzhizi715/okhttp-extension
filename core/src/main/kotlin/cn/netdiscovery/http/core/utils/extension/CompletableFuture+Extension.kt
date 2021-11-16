package cn.netdiscovery.http.core.utils.extension

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`CompletableFuture+Extension`
 * @author: Tony Shen
 * @date: 2020-10-09 01:57
 * @version: V1.0 <描述当前版本功能>
 */
fun Call.Factory.asyncCall(request: Request): CompletableFuture<Response>  {
    val future = CompletableFuture<Response>()
    val call = newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            future.complete(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            future.completeExceptionally(e)
        }
    })

    return future
}

fun <T> List<CompletableFuture<T>>.collect(): CompletableFuture<List<T>> {
    val allFutures = CompletableFuture.allOf(*this.toTypedArray())
    return allFutures.thenApply {
        this.map(CompletableFuture<T>::join)
    }
}