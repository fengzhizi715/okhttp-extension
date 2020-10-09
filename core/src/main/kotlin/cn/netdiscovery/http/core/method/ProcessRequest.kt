package cn.netdiscovery.http.core.method

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.config.jsonMediaType
import cn.netdiscovery.http.core.domain.RequestMethodModel
import okhttp3.FormBody
import okhttp3.RequestBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.method.ProcessRequest
 * @author: Tony Shen
 * @date: 2020-10-09 00:46
 * @version: V1.0 <描述当前版本功能>
 */
fun processQuery(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    var url = requestMethodModel.getUrl()
    val query = params.joinToString("&") { "${it.first}=${it.second}" }
    if(query.isNotEmpty()) {
        url = "$url?$query"
        requestMethodModel.setUrl(url)
    }
    return requestMethodModel
}

fun processPath(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    var url = requestMethodModel.getUrl()

    params.forEach {
        val name = it.first
        val value = it.second
        url = url.replace("{$name}", value)
    }
    requestMethodModel.setUrl(url)
    return requestMethodModel
}

fun processHeaders(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    params.forEach {
        val name = it.first
        val value = it.second

        requestMethodModel.requestBuilder.addHeader(name, value)
    }
    return requestMethodModel
}

fun processBody(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    val requestBodyBuilder = FormBody.Builder()
    params.forEach {
        val name = it.first
        val value = it.second

        requestBodyBuilder.add(name, value)
    }
    requestMethodModel.requestBody = requestBodyBuilder.build()
    return requestMethodModel
}

fun processJson(json: String, requestMethodModel: RequestMethodModel): RequestMethodModel {
    val requestBody = RequestBody.create(jsonMediaType, json)
    requestMethodModel.requestBody = requestBody
    return requestMethodModel
}

fun processJson(json: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    if (json.size > 1)
        throw IllegalArgumentException("Invalid json")

    val jsonString = json.first().second
    return processJson(jsonString, requestMethodModel)
}
