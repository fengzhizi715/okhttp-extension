package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.AbstractHttpService
import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.test.domain.RequestModel
import cn.netdiscovery.http.core.test.domain.ResponseData
import cn.netdiscovery.http.core.test.domain.ResponseDataMapper
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.ApiService
 * @author: Tony Shen
 * @date: 2020-10-21 11:28
 * @version: V1.0 自定义的 APIService
 */
class ApiService(client: HttpClient) : AbstractHttpService(client) {

    fun testGet(name: String) = get<Response> {
        url = "/sayHi/$name"
    }

    fun testGetWithPath(path: Map<String, String>) = get<Response> {
        url = "/sayHi/{name}"
        pathParams = Params.from(path)
    }

    fun testGetWithHeader(headers: Map<String, String>) = get<Response> {
        url = "/response-headers"
        headersParams = Params.from(headers)
    }

    fun testGetWithHeaderAndQuery(headers: Map<String, String>, queries: Map<String,String>) = get<Response> {
        url = "/response-headers-queries"
        headersParams = Params.from(headers)
        queriesParams = Params.from(queries)
    }

    fun testPost(body: Params) = post<Response> {
        url = "/response-body"
        bodyParams = body
    }

    fun testPostWithModel(model: RequestModel) = post<Response>{
        url = "/response-body"
        bodyModel = model
    }

    fun testPostWithJsonModel(model: RequestModel) = jsonPost<Response>{
        url = "/response-body-with-model"
        jsonModel = model
    }

    fun testPostWithResponseMapper(model: RequestModel) = jsonPost<ResponseData>{
        url = "/response-body-with-model"
        jsonModel = model
        responseMapper = ResponseDataMapper::class
    }

    fun testPostWithoutResponseMapper(model: RequestModel) = jsonPost<ResponseData>{
        url = "/response-body-with-model"
        jsonModel = model
    }
}