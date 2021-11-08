package cn.netdiscovery.http.core.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptor.SigningInterceptor
 * @author: Tony Shen
 * @date: 2021/11/8 11:14 上午
 * @version: V1.0 <描述当前版本功能>
 */
class SigningInterceptor(private val parameterName: String, private val signer: HttpUrl.() -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =

        with(chain.request()) {
            val signedKey = signer(url)

            val requestUrl = with(url.newBuilder()) {
                addQueryParameter(parameterName, signedKey)
                build()
            }

            with(newBuilder()) {
                url(requestUrl)
                chain.proceed(build())
            }
        }
}