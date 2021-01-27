package cn.netdiscovery.http.core.test.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.retrofit.ApiService
 * @author: Tony Shen
 * @date: 2021-01-27 10:35
 * @version: V1.0 <描述当前版本功能>
 */
interface ApiService {

    @GET("/response-headers")
    fun responseHeaders(@HeaderMap headers:Map<String, String>):Observable<Map<String, List<String>>>

    @GET("/response-headers-queries")
    fun responseHeadersQueries(@HeaderMap headers:Map<String, String>, @QueryMap paramas:Map<String, String>):Observable<Map<String, List<String>>>
}