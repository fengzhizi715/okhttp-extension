package cn.netdiscovery.http.core.utils.extension

import okhttp3.*
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`Call+Extension`
 * @author: Tony Shen
 * @date: 2020-10-03 11:17
 * @version: V1.0 <描述当前版本功能>
 */
fun Call.Factory.call(block:()-> Request):Response = this.newCall(block.invoke()).execute()

fun Call.blockStringBody(): String? = execute().stringBody()

fun <T> Call.letStringBody(block: (body: String?) -> T): T = execute().letStringBody(block)

fun <T> Call.letBody(block: (body: ResponseBody?) -> T): T = execute().letBody(block)

fun <T> Call.blockAndClose(block: (response: Response) -> T): T = execute().closeAfter(block)

fun Call.applyBody(block: (body: ResponseBody?) -> Unit) = execute().applyBody(block)

fun Call.applyStringBody(block: (body: String?) -> Unit) = execute().applyStringBody(block)

fun Call.executeAsync(): CompletableFuture<Response> {
    val future = CompletableFuture<Response>()
    this.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            future.complete(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            future.completeExceptionally(e)
        }
    })

    return future
}