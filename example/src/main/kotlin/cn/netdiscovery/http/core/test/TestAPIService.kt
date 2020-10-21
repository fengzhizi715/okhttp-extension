package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.AbstractHttpService
import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.TestAPIService
 * @author: Tony Shen
 * @date: 2020-10-21 11:28
 * @version: V1.0 <描述当前版本功能>
 */
class TestAPIService(client: HttpClient) : AbstractHttpService(client) {

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

    fun testPostWithModel(model: RequestBody) = post<Response>{
        url = "/response-body"
        bodyModel = model
    }

    fun testPostWithJsonModel(model: RequestBody) = jsonPost<Response>{
        url = "/response-body-with-model"
        jsonModel = model
    }

    fun testPostWithResponseMapper(model: RequestBody) = jsonPost<ResponseData>{
        url = "/response-body-with-model"
        jsonModel = model
        responseMapper = ResponseDataMapper::class
    }
}