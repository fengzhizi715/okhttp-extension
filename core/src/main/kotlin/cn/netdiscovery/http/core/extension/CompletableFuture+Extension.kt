package cn.netdiscovery.http.core.extension

import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`CompletableFuture+Extension`
 * @author: Tony Shen
 * @date: 2020-10-09 01:57
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> List<CompletableFuture<T>>.collect(): CompletableFuture<List<T>> {
    val allFutures = CompletableFuture.allOf(*this.toTypedArray())
    return allFutures.thenApply {
        this.map(CompletableFuture<T>::join)
    }
}