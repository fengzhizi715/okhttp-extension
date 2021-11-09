package cn.netdiscovery.http.core.dsl

import okhttp3.Interceptor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.InterceptorsDsl
 * @author: Tony Shen
 * @date: 2021/11/9 1:15 下午
 * @version: V1.0 <描述当前版本功能>
 */
class InterceptorsDsl(private val interceptors: MutableList<Interceptor> = mutableListOf()) {

    operator fun Interceptor.unaryPlus() {
        interceptors += this
    }

    fun list(): List<Interceptor> = this.interceptors
}