package cn.netdiscovery.http.core.extension

import cn.netdiscovery.http.core.domain.response.ResponseConsumer
import okhttp3.Response
import okhttp3.ResponseBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.extension.`Response+Extension`
 * @author: Tony Shen
 * @date: 2020-10-03 11:11
 * @version: V1.0 <描述当前版本功能>
 */
fun Response.stringBody(): String? = this.body?.string()

fun <T> Response.closeAfter(block: (response: Response) -> T): T = block.invoke(this).apply { close() }

fun Response.applyBody(block: (body: ResponseBody?) -> Unit) {
    block.invoke(this.body)
}

fun Response.applyStringBody(block: (body: String?) -> Unit) {
    block.invoke(this.stringBody())
}

fun <T> Response.letBody(block: (body: ResponseBody?) -> T): T = block.invoke(this.body)

fun <T> Response.letStringBody(block: (body: String?) -> T): T = block.invoke(this.stringBody())

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