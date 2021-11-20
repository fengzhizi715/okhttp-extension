package cn.netdiscovery.http.core.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @FileName:
 *          cn.netdiscovery.http.core.interceptor.SigningInterceptor
 * @author: Tony Shen
 * @date: 2021/11/8 11:14 上午
 * @version: V1.0 请求签名的拦截器，支持对 query 参数进行签名。
 */
class SigningInterceptor(private val parameterName: String,
                         private val extraMap: Map<String, String>? =null,
                         private val signer: Request.() -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =

        with(chain.request()) {
            val signedKey = signer(this)

            val requestUrl = with(url.newBuilder()) {
                addQueryParameter(parameterName, signedKey)
                extraMap?.let {
                    it.forEach { (k, v) ->  addQueryParameter(k, v)}
                }
                build()
            }

            with(newBuilder()) {
                url(requestUrl)
                chain.proceed(build())
            }
        }
}