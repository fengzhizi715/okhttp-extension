package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.AbstractHttpService
import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.test.converter.GsonConverter
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
import cn.netdiscovery.http.extension.rxjava3.asObservable
import cn.netdiscovery.http.interceptor.LoggingInterceptor
import cn.netdiscovery.http.interceptor.log.LogManager
import cn.netdiscovery.http.interceptor.log.LogProxy
import okhttp3.Response


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.Test
 * @author: Tony Shen
 * @date: 2020-10-11 01:20
 * @version: V1.0 <描述当前版本功能>
 */
val httpClient by lazy {
    LogManager.logProxy(object : LogProxy {  // 必须要实现 LogProxy ，否则无法打印网络请求的 request 、response
        override fun e(tag: String, msg: String) {
        }

        override fun w(tag: String, msg: String) {
        }

        override fun i(tag: String, msg: String) {
            println("$tag:$msg")
        }

        override fun d(tag: String, msg: String) {
            println("$tag:$msg")
        }
    })

    val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(true) // TODO: 发布到生产环境需要改成false
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
//        .hideVerticalLine()// 隐藏竖线边框
            .build()

    HttpClientBuilder()
            .baseUrl("http://localhost:8080")
            .addInterceptor(loggingInterceptor)
            .converter(GsonConverter())
            .jsonConverter(GlobalRequestJSONConverter::class)
            .build()
}

val apiService by lazy {
    TestAPIService(httpClient)
}

fun main() {
    testPostWithModel()
}

fun testGet() {
    apiService.testGet("Tony").sync()
}

fun testGetWithPath() {
    val path = mutableMapOf<String, String>()
    path["name"] = "Tony"
    apiService.testGetWithPath(path).sync()
}

fun testGetWithHeader() {
    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"
    apiService.testGetWithHeader(header).sync()
}

fun testGetWithHeaderAndQuery() {
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

fun testPost() {
    val body = params(
        "name1" to "value1",
        "name2" to "value2",
        "name3" to "value3"
    )
    apiService.testPost(body).sync()
}

fun testPostWithModel() {
    val requestBody = RequestBody()
    apiService.testPostWithModel(requestBody).sync()
}

fun testPostWithJsonModel() {
    val requestBody = RequestBody()
    apiService.testPostWithJsonModel(requestBody).sync()
}

fun testPostWithResponseMapper() {
    val requestBody = RequestBody()
    apiService.testPostWithResponseMapper(requestBody).sync()
}

fun testPostWithResponseMapperAndObservable() {
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