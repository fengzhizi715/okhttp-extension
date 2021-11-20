package cn.netdiscovery.http.core.interceptors.trace

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptor.TraceIdProvider
 * @author: Tony Shen
 * @date: 2021-01-27 10:07
 * @version: V1.0 <描述当前版本功能>
 */
interface TraceIdProvider {

    fun getTraceId():String
}