package cn.netdiscovery.http.core.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.RetryInterceptor
 * @author: Tony Shen
 * @date: 2021/11/29 1:25 下午
 * @version: V1.0 重试的 拦截器，可配置重试次数、间隔时间等
 */
class RetryInterceptor(
    private val failureThreshold: Int = 3,
    private val invocationTimeout: Long = 0,
    private val errorStatuses: List<Int> = listOf(503, 504)
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attemptsCount = 0
        while (true) {
            try {
                if (shouldDelay(attemptsCount)) {
                    performAndReturnDelay()
                }
                val response = chain.proceed(request)
                if (!isRetry(response, attemptsCount)) return response
            } catch (e: SocketTimeoutException) {
                if (attemptsCount >= failureThreshold) throw e
            }
            attemptsCount++
        }
    }

    private fun performAndReturnDelay() {
        try {
            Thread.sleep(invocationTimeout)
        } catch (ignored: InterruptedException) {
        }
    }

    private fun shouldDelay(attemptsCount: Int) = invocationTimeout > 0 && attemptsCount > 0

    private fun isRetry(response: Response, attemptsCount: Int): Boolean = attemptsCount < failureThreshold && response.code in errorStatuses
}