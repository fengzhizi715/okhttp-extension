package cn.netdiscovery.http.extension.coroutines

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.coroutines.`Call+Extension`
 * @author: Tony Shen
 * @date: 2021-11-09 01:47
 * @version: V1.0 <描述当前版本功能>
 */
suspend fun Call.suspendCall(): Response =
    suspendCancellableCoroutine { cont ->
        cont.invokeOnCancellation {
            this.cancel()
        }
        this.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                cont.resume(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }
        })
    }

suspend fun Call.Factory.suspendCall(request: Request): Response =
    suspendCancellableCoroutine { cont ->
        val call = newCall(request)
        cont.invokeOnCancellation {
            call.cancel()
        }
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                cont.resume(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }
        })
    }

fun Call.Factory.flowCall(request: Request): Flow<Response> = callbackFlow {
    val call = newCall(request)
    call.enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            trySendBlocking(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            close()
        }
    })
    awaitClose()
}