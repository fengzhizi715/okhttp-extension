package cn.netdiscovery.http.core.dsl.context

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.context.MultipartBodyContext
 * @author: Tony Shen
 * @date: 2021/11/16 1:47 下午
 * @version: V1.0 <描述当前版本功能>
 */
@HttpDslMarker
class MultipartBodyContext(type: String?) {

    private val mediaType = type?.let { it.toMediaType() }
    private val builder = MultipartBody.Builder().also { builder ->
        mediaType?.let { builder.setType(mediaType) }
    }

    operator fun MultipartBody.Part.unaryPlus() {
        builder.addPart(this)
    }

    fun part(name: String, filename: String? = null, init: BodyContext.() -> RequestBody): MultipartBody.Part =
        MultipartBody.Part.createFormData(name, filename, BodyContext(null).init())


    fun build(): MultipartBody = builder.build()
}