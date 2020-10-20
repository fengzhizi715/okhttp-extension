package cn.netdiscovery.http.core.interceptor

import cn.netdiscovery.http.core.aop.ProcessorStore
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.nio.charset.Charset

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptor.ResponseProcessingInterceptor
 * @author: Tony Shen
 * @date: 2020-10-03 13:36
 * @version: V1.0 <描述当前版本功能>
 */
class ResponseProcessingInterceptor(private val processorStore: ProcessorStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request= chain.request()
        val response= chain.proceed(request)
        val body= response.body

        val source = body?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source?.buffer
        val stringBody = buffer?.clone()?.readString(Charset.forName("UTF-8")).toString()

        val responseProcessors = processorStore.getResponseProcessors()

        if (responseProcessors.isEmpty())
            return response

        responseProcessors
                .forEach { processor ->
                    val parsableResponse = response.newBuilder()
                            .body(stringBody.toResponseBody(response.body?.contentType()))
                            .build()

                    processor.process(parsableResponse)
                }
        return response
    }
}