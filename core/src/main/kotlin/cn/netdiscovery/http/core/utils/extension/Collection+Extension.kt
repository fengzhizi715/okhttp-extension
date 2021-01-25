package cn.netdiscovery.http.core.utils.extension

import cn.netdiscovery.http.core.domain.ResponseConsumer
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`Collection+Extension`
 * @author: Tony Shen
 * @date: 2021-01-21 19:18
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> Collection<ResponseConsumer<T>?>.collect(): ResponseConsumer<T> {
    val responses: MutableList<Response> = mutableListOf()
    this.forEach {
        if (it?.response != null)
            responses.add(it.response)

        if (it?.responses != null)
            responses.addAll(it.responses!!)
    }

    val responseModels: MutableList<T> = mutableListOf()
    this.forEach {
        if (it?.responseModel != null)
            responseModels.add(it.responseModel)

        if (it?.responseModels != null)
            responseModels.addAll(it.responseModels!!)
    }

    return ResponseConsumer(
        responses = if (responses.isEmpty()) null else responses,
        responseModels = if (responseModels.isEmpty()) null else responseModels
    )
}