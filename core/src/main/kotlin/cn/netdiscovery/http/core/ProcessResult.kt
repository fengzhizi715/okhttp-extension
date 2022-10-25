package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.domain.ResponseConsumer
import cn.netdiscovery.http.core.request.method.RequestMethodProcessor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.ProcessResult
 * @author: Tony Shen
 * @date: 2020-10-04 13:31
 * @version: V1.0 <描述当前版本功能>
 */
class ProcessResult<T : Any>(private val methodProcessor: RequestMethodProcessor<out T>) {

    /**
     * 同步调用，使用 Call.execute()
     *
     * @see okhttp3.Call
     */
    fun sync(): T = methodProcessor.process().getResult()

    /**
     * 异步调用，使用 Call.enqueue()
     * 采用 Active Object 模式
     *
     * @see okhttp3.Call
     */
    fun async(): CompletableFuture<T> = methodProcessor.processAsync().thenApply(ResponseConsumer<out T>::getResult)

    /**
     * 默认使用异步方式
     */
    @Throws(InterruptedException::class, ExecutionException::class)
    fun get():T = async().get()

    /**
     * 取消请求的调用
     */
    fun cancel() {
        methodProcessor.cancel()
    }
}