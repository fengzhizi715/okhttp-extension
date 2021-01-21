package cn.netdiscovery.http.core.request

import cn.netdiscovery.http.core.config.jsonMediaType
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethodModel
import okhttp3.FormBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.method.ProcessRequest
 * @author: Tony Shen
 * @date: 2020-10-09 00:46
 * @version: V1.0 <描述当前版本功能>
 */
fun processQuery(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {

    return requestMethodModel.apply {
        val url = requestMethodModel.getUrl()

        val query = params.joinToString("&") { "${it.first}=${it.second}" }
        if (query.isNotEmpty()) {
            this.setUrl("$url?$query")
        }
    }
}

fun processPath(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {

    return requestMethodModel.apply {
        var url = requestMethodModel.getUrl()

        params.forEach {
            val name = it.first
            val value = it.second
            url = url.replace("{$name}", value)
        }
        requestMethodModel.setUrl(url)
    }
}

fun processHeaders(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {

    return requestMethodModel.apply {
        params.forEach {
            val name = it.first
            val value = it.second
            this.requestBuilder.addHeader(name, value)
        }
    }
}

fun processBody(params: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {

    return requestMethodModel.apply {
        val requestBodyBuilder = FormBody.Builder()
        params.forEach {
            val name = it.first
            val value = it.second
            requestBodyBuilder.add(name, value)
        }
        this.requestBody = requestBodyBuilder.build()
    }
}

fun processJson(json: Params, requestMethodModel: RequestMethodModel): RequestMethodModel {
    if (json.size > 1)
        throw IllegalArgumentException("Invalid json")

    val jsonString = json.first().second
    return processJson(jsonString, requestMethodModel)
}

private fun processJson(json: String, requestMethodModel: RequestMethodModel): RequestMethodModel {

    return requestMethodModel.apply {
        this.requestBody = json.toRequestBody(jsonMediaType)
    }
}
