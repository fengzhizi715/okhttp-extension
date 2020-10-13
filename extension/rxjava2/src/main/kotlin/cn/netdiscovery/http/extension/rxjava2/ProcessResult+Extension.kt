package cn.netdiscovery.http.extension.rxjava2

import cn.netdiscovery.http.core.domain.ProcessResult
import io.reactivex.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.rxjava2.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-10-13 21:05
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.asObservable(): Observable<T> {

    return Observable.create {
        it.onNext(sync() as T)
    }
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