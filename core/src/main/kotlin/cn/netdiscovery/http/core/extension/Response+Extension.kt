package cn.netdiscovery.http.core.extension

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