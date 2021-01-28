package cn.netdiscovery.http.core.test.feign

import cn.netdiscovery.http.core.test.domain.RequestModel
import cn.netdiscovery.http.core.test.domain.WrapperResponse
import feign.*


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.feign.ApiService
 * @author: Tony Shen
 * @date: 2021-01-28 21:06
 * @version: V1.0 <描述当前版本功能>
 */
interface ApiService {

    @RequestLine("GET /response-headers")
    fun responseHeaders(@HeaderMap headers:Map<String, String>): Map<String, List<String>>

    @RequestLine("GET /response-headers-queries")
    fun responseHeadersQueries(@HeaderMap headers:Map<String, String>, @QueryMap paramas:Map<String, String>): Map<String, List<String>>

    @RequestLine("POST /response-body-with-model")
    @Headers("Content-Type: application/json")
    @Body("{body}")
    fun responseBodyWithModel(@Param("body") model: RequestModel): WrapperResponse
}