package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.AbstractHttpService
import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.extension.rxjava3.asObservable
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.TestAPIService
 * @author: Tony Shen
 * @date: 2020-10-21 11:28
 * @version: V1.0 <描述当前版本功能>
 */
fun testGet(apiService:TestAPIService) {
    apiService.testGet("Tony").sync()
}

fun testGetWithPath(apiService:TestAPIService) {
    val path = mutableMapOf<String, String>()
    path["name"] = "Tony"
    apiService.testGetWithPath(path).sync()
}

fun testGetWithHeader(apiService:TestAPIService) {
    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"
    apiService.testGetWithHeader(header).sync()
}

fun testGetWithHeaderAndQuery(apiService:TestAPIService) {
    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"

    val queries = mutableMapOf<String, String>()
    queries["q1"] = "a"
    queries["q2"] = "b"
    queries["q3"] = "c"
    apiService.testGetWithHeaderAndQuery(header,queries).sync()
}

fun testPost(apiService:TestAPIService) {
    val body = params(
            "name1" to "value1",
            "name2" to "value2",
            "name3" to "value3"
    )
    apiService.testPost(body).sync()
}

fun testPostWithModel(apiService:TestAPIService) {
    val requestBody = RequestBody()
    apiService.testPostWithModel(requestBody).sync()
}

fun testPostWithJsonModel(apiService:TestAPIService) {
    val requestBody = RequestBody()
    apiService.testPostWithJsonModel(requestBody).sync()
}

fun testPostWithResponseMapper(apiService:TestAPIService) {
    val requestBody = RequestBody()
    apiService.testPostWithResponseMapper(requestBody).sync()
}

fun testPostWithResponseMapperAndObservable(apiService:TestAPIService) {
    val requestBody = RequestBody()
    apiService.testPostWithResponseMapper(requestBody)
            .asObservable<ResponseData>()
            .subscribe {
                println(it.content)
            }
}

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