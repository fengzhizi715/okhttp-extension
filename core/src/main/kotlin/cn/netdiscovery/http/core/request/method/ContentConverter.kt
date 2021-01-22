package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.request.converter.DefaultRequestContentConverter
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.content.Content
import cn.netdiscovery.http.core.domain.params
import kotlin.reflect.full.primaryConstructor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.method.ContentConverter
 * @author: Tony Shen
 * @date: 2020-10-09 01:33
 * @version: V1.0 <描述当前版本功能>
 */
object ContentConverter {

    private val defaultConverter = DefaultRequestContentConverter()

    fun convert(content: Content): Params {
        val params = content.params ?: params()

        if (content.map != null) {
            val mapParams = Params.from(content.map)
            params.addAll(mapParams)
        }

        if (content.model != null) {
            val converter = if (content.modelConverter != null) {
                content.modelConverter.primaryConstructor?.call() // 基于反射创建 content.modelConverter 对象
            } else {
                defaultConverter
            }

            val modelParams = converter?.convert(content) ?: params()
            params.addAll(modelParams)
        }

        return params
    }
}