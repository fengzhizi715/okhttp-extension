package cn.netdiscovery.http.extension.rxjava3

import cn.netdiscovery.http.core.ProcessResult
import io.reactivex.rxjava3.core.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.rxjava3.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-10-13 16:47
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.asObservable(): Observable<T> = Observable.create {
    it.onNext(sync() as T)
}

fun <T> ProcessResult<out Any>.asFlowable(): Flowable<T> = Flowable.create({
    it.onNext(sync() as T)
}, BackpressureStrategy.BUFFER)

fun <T> ProcessResult<out Any>.asSingle(): Single<T> = Single.create {
    it.onSuccess(sync() as T)
}

fun <T> ProcessResult<out Any>.asMaybe(): Maybe<T> = Maybe.create {
    it.onSuccess(sync() as T)
}