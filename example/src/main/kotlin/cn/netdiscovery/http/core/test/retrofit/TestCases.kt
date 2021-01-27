package cn.netdiscovery.http.core.test.retrofit

import cn.netdiscovery.http.core.test.domain.RequestModel
import cn.netdiscovery.http.core.test.domain.WrapperResponse
import io.reactivex.rxjava3.core.Observable

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.retrofit.TestCases
 * @author: Tony Shen
 * @date: 2021-01-27 14:02
 * @version: V1.0 <描述当前版本功能>
 */
fun responseHeaders(): Observable<Map<String, List<String>>> {

    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"

    return apiService.responseHeaders(header)
}

fun responseHeadersQueries():Observable<Map<String, List<String>>> {

    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"

    val queries = mutableMapOf<String, String>()
    queries["q1"] = "a"
    queries["q2"] = "b"
    queries["q3"] = "c"

    return apiService.responseHeadersQueries(header,queries)
}

fun responseBodyWithModel():Observable<WrapperResponse> {

    val requestModel = RequestModel()

    return apiService.responseBodyWithModel(requestModel)
}