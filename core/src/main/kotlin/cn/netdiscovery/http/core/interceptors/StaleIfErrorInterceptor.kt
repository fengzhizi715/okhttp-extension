package cn.netdiscovery.http.core.interceptors

import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.CacheControl.Builder

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.StaleIfErrorInterceptor
 * @author: Tony Shen
 * @date: 2021/11/24 5:47 下午
 * @version: V1.0 stale-if-error 拦截器，如果原始服务器不可访问，它允许使用之前的缓存响应。
 */
class StaleIfErrorInterceptor @JvmOverloads constructor(
    staleDuration: Int = DEFULAT_MAX_STALE_DURATION,
    staleDurationTimeUnit: TimeUnit? = DEFULAT_MAX_STALE_TIMEUNIT
) : Interceptor {
    private val staleDuration: Int
    private val staleDurationTimeUnit: TimeUnit?

    init {
        // Preconditions check
        if (staleDuration <= 0) throw AssertionError()
        if (staleDurationTimeUnit == null) throw AssertionError()
        this.staleDuration = staleDuration
        this.staleDurationTimeUnit = staleDurationTimeUnit
    }

    private fun getMaxStaleDuration(): Int {
        return if (staleDuration != -1) staleDuration else DEFULAT_MAX_STALE_DURATION
    }

    private fun getMaxStaleDurationTimeUnit(): TimeUnit {
        return staleDurationTimeUnit ?: DEFULAT_MAX_STALE_TIMEUNIT
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        val request: Request = chain.request()

        // first try the regular (network) request, guard with try-catch
        // so we can retry with force-cache below
        try {
            response = chain.proceed(request)

            // return the original response only if it succeeds
            if (response.isSuccessful) {
                return response
            }
        } catch (e: Exception) {
            // original request error
        }
        if (response == null || !response.isSuccessful) {
            val cacheControl: CacheControl = Builder().onlyIfCached()
                .maxStale(getMaxStaleDuration(), getMaxStaleDurationTimeUnit()).build()
            val newRequest: Request = request.newBuilder().cacheControl(cacheControl).build()
            response = try {
                chain.proceed(newRequest)
            } catch (e: Exception) { // cache not available
                throw e
            }
        }
        return response!!
    }

    companion object {
        // based on code from https://github.com/square/okhttp/issues/1083 by jvincek
        private const val DEFULAT_MAX_STALE_DURATION = 28
        private val DEFULAT_MAX_STALE_TIMEUNIT = TimeUnit.DAYS
    }
}