package cn.netdiscovery.http.core.interceptors.jwt

import cn.netdiscovery.http.core.config.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.jwt.JWTInterceptor
 * @author: Tony Shen
 * @date: 2021/11/20 1:45 下午
 * @version: V1.0 <描述当前版本功能>
 */
class JWTInterceptor(private val provider: JWTProvider): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
            .newBuilder()
            .apply {
                val token = provider.getJWTToken()
                removeHeader(AUTHORIZATION)
                addHeader(AUTHORIZATION, "Bearer $token")
            }
            .build()

        var response = chain.proceed(request)

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            synchronized(this) {
                //如果返回了401，则尝试通过 provider 获取新的 token
                val accessToken = provider.refreshToken()
                if (!accessToken.isNullOrEmpty()) {
                    //如果获取到了新的token，则构建一个新的请求
                    request = request.newBuilder()
                        .removeHeader(AUTHORIZATION)
                        .addHeader(AUTHORIZATION, "Bearer $accessToken")
                        .build()
                    response.close()

                    return chain.proceed(request)
                }
            }

        }
        return response
    }
}