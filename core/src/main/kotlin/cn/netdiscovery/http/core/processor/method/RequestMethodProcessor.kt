package cn.netdiscovery.http.core.processor.method

import cn.netdiscovery.http.core.domain.response.ResponseConsumer
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.method.RequestMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-03 20:37
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestMethodProcessor<T : Any> {

    fun process(): ResponseConsumer<T>

    fun processAsync(): CompletableFuture<ResponseConsumer<T>>
}