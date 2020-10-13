package cn.netdiscovery.http.extension.rxjava3

import cn.netdiscovery.http.core.domain.ProcessResult
import io.reactivex.rxjava3.core.Observable

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.rxjava3.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-10-13 16:47
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.asObservable(): Observable<T> {

    return Observable.create {
        it.onNext(sync() as T)
    }
}