package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.core.test.domain.RequestBody
import cn.netdiscovery.http.core.test.domain.ResponseData
import cn.netdiscovery.http.extension.rxjava3.asObservable

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.TestCases
 * @author: Tony Shen
 * @date: 2020-12-14 17:24
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