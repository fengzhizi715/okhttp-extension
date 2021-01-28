package cn.netdiscovery.http.core.test.feign

import cn.netdiscovery.http.core.test.httpClient
import feign.Feign
import feign.gson.GsonDecoder
import feign.okhttp.OkHttpClient

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.feign.FeignManager
 * @author: Tony Shen
 * @date: 2021-01-28 21:24
 * @version: V1.0 <描述当前版本功能>
 */
val apiService: ApiService by lazy {
    Feign.builder()
        .client(OkHttpClient(httpClient.okHttpClient()))
        .decoder(GsonDecoder())
        .target(ApiService::class.java, "http://localhost:8080")
}