package cn.netdiscovery.http.extension.rxjava2

import cn.netdiscovery.http.core.response.ResponseMapper
import io.reactivex.*
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.rxjava2.`Response+Extension`
 * @author: Tony Shen
 * @date: 2021/12/22 1:05 AM
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> Response.asObservable(mapper: ResponseMapper<T>): Observable<T> = Observable.create {
    it.onNext(mapper.map(this))
}

fun <T>Response.asFlowable(mapper: ResponseMapper<T>): Flowable<T> = Flowable.create({
    it.onNext(mapper.map(this))
}, BackpressureStrategy.BUFFER)

fun <T> Response.asSingle(mapper: ResponseMapper<T>): Single<T> = Single.create {
    it.onSuccess(mapper.map(this))
}

fun <T> Response.asMaybe(mapper: ResponseMapper<T>): Maybe<T> = Maybe.create {
    it.onSuccess(mapper.map(this))
}