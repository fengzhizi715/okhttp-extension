package cn.netdiscovery.http.core.dsl.context

import cn.netdiscovery.http.core.config.formMediaType
import cn.netdiscovery.http.core.config.jsonMediaType
import cn.netdiscovery.http.core.dsl.Form
import cn.netdiscovery.http.core.dsl.Json
import cn.netdiscovery.http.core.serializer.SerializerManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import okhttp3.internal.EMPTY_RESPONSE
import java.io.File

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.BodyContext
 * @author: Tony Shen
 * @date: 2021/11/16 11:28 上午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
class BodyContext(type: String?) {

    private val mediaType = type?.let { it.toMediaType() }

    fun string(content: String): RequestBody = content.toRequestBody(mediaType)

    fun file(content: File): RequestBody = content.asRequestBody(mediaType)

    fun bytes(content: ByteArray): RequestBody = content.toRequestBody(mediaType)

    fun json(content: String): RequestBody = content.toRequestBody(jsonMediaType)

    fun form(content: String): RequestBody = content.toRequestBody(formMediaType)

    fun json(init: Json.() -> Unit): RequestBody = Json().also(init).toString().toRequestBody(jsonMediaType)

    fun form(init: Form.() -> Unit): RequestBody = Form().also(init).buildBody()

    fun json(any: Any): RequestBody = SerializerManager.toJson(any)?.toRequestBody(jsonMediaType)?: EMPTY_REQUEST
}