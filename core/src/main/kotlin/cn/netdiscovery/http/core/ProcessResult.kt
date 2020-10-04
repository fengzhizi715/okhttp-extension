package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.processor.method.RequestMethodProcessor
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.ProcessResult
 * @author: Tony Shen
 * @date: 2020-10-04 13:31
 * @version: V1.0 <描述当前版本功能>
 */
class ProcessResult <T : Any>(private val methodProcessor: RequestMethodProcessor<out T>) {

    /**
     * Blocking send, used Call.execute()
     *
     * @see okhttp3.Call
     */
    fun sync(): T {
        return methodProcessor.process().getResult()
    }

    /**
     * Async send, used Call.enqueue()
     *
     * @see okhttp3.Call
     */
    fun async(): CompletableFuture<T> {
        return methodProcessor.processAsync()
                .thenApply(ResponseConsumer<out T>::getResult)
    }
}