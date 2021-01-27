package cn.netdiscovery.http.core.test.retrofit

import cn.netdiscovery.http.core.test.httpClient
import cn.netdiscovery.http.core.test.loggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.retrofit.RetrofitManager
 * @author: Tony Shen
 * @date: 2021-01-27 10:48
 * @version: V1.0 <描述当前版本功能>
 */
const val DEFAULT_CONN_TIMEOUT = 30

val apiService:ApiService by lazy {
    val retrofit = Retrofit.Builder()
        .client(httpClient.okHttpClient())
        .baseUrl("http://localhost:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()

    retrofit.create(ApiService::class.java)
}