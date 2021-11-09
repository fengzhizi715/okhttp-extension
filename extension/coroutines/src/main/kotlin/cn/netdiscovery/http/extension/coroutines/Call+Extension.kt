package cn.netdiscovery.http.extension.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
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