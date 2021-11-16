package cn.netdiscovery.http.core.dsl

import okhttp3.FormBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.Form
 * @author: Tony Shen
 * @date: 2021/11/16 7:24 下午
 * @version: V1.0 <描述当前版本功能>
 */
class Form {

    private val bodyBuilder = FormBody.Builder()

    infix fun String.to(v: Any) {
        bodyBuilder.add(this, v.toString())
    }

    fun addEncoded(k: String, v: String) {
        bodyBuilder.addEncoded(k, v)
    }

    fun buildBody(): FormBody = bodyBuilder.build()
}