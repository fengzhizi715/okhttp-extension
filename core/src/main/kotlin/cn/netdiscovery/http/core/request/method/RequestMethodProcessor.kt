package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.Cancelable
import cn.netdiscovery.http.core.domain.ResponseConsumer
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.method.RequestMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-03 20:37
 * @version: V1.0 <描述当前版本功能>
 */
interface RequestMethodProcessor<T : Any> : Cancelable {

    fun process(): ResponseConsumer<T>

    fun processAsync(): CompletableFuture<ResponseConsumer<T>>
}