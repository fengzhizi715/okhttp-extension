package cn.netdiscovery.http.extension.reactor

import cn.netdiscovery.http.core.ProcessResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.reactor.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-12-25 17:56
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.asFlux(): Flux<T> = Flux.create {
    it.next(sync() as T)
}

fun <T> ProcessResult<out Any>.asMono(): Mono<T> = Mono.create {
    it.success(sync() as T)
}