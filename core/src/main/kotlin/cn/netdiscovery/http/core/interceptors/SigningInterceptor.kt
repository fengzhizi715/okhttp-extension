package cn.netdiscovery.http.core.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.SigningInterceptor
 * @author: Tony Shen
 * @date: 2021/11/8 11:14 上午
 * @version: V1.0 请求签名的拦截器，支持对 query 参数进行签名。
 */
class SigningInterceptor(private val parameterName: String,                // sign 的 参数名，放在 url 的 query 中
                         private val extraMap: Map<String, String>? =null, // query 后面添加额外的一些参数
                         private val signer: Request.() -> String          // 对请求进行签名
) : Interceptor {
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