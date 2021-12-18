package cn.netdiscovery.http.core.test.okhttpextension

import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.core.test.domain.RequestModel
import cn.netdiscovery.http.core.test.domain.ResponseData
import cn.netdiscovery.http.extension.rxjava3.asObservable

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.okhttpextension.TestCases
 * @author: Tony Shen
 * @date: 2020-12-14 17:24
 * @version: V1.0 <描述当前版本功能>
 */
fun testGet(apiService: ApiService) {
    apiService.testGet("Tony").sync()
}

fun testGetWithPath(apiService: ApiService) {
    val path = mutableMapOf<String, String>()
    path["name"] = "Tony"
    apiService.testGetWithPath(path).sync()
}

fun testGetWithHeader(apiService: ApiService) {
    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"
    apiService.testGetWithHeader(header).sync()
}

fun testGetWithHeaderAndQuery(apiService: ApiService) {
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

fun testPost(apiService: ApiService) {
    val body = params(
        "name1" to "value1",
        "name2" to "value2",
        "name3" to "value3"
    )
    apiService.testPost(body).sync()
}

fun testPostWithModel(apiService: ApiService) {
    val requestModel = RequestModel()
    apiService.testPostWithModel(requestModel).sync()
}

fun testPostWithJsonModel(apiService: ApiService) {
    val requestModel = RequestModel()
    apiService.testPostWithJsonModel(requestModel).sync()
}

fun testPostWithResponseMapper(apiService: ApiService) {
    val requestModel = RequestModel()
    apiService.testPostWithResponseMapper(requestModel).sync()
}

fun testPostWithoutResponseMapper(apiService: ApiService) {
    val requestModel = RequestModel()
    apiService.testPostWithoutResponseMapper(requestModel).sync()
}

fun testPostWithResponseMapperAndObservable(apiService: ApiService) {
    val requestModel = RequestModel()
    apiService.testPostWithResponseMapper(requestModel)
        .asObservable<ResponseData>()
        .subscribe {
            println(it.content)
        }
}