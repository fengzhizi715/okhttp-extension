package cn.netdiscovery.http.core.interceptor

import cn.netdiscovery.http.core.config.TRACE_ID
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptor.TraceIdInterceptor
 * @author: Tony Shen
 * @date: 2021-01-26 18:19
 * @version: V1.0 <描述当前版本功能>
 */
class TraceIdInterceptor(private val traceIdProvider: TraceIdProvider): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request= chain.request()

        request = request.newBuilder()
            .header(TRACE_ID, traceIdProvider.getTraceId())
            .build()
        //继续发送原始请求
        return chain.proceed(request)
    }
}