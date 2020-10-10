package cn.netdiscovery.http.core.domain.content

import cn.netdiscovery.http.core.converter.DefaultRequestModelConverter
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.params
import kotlin.reflect.full.primaryConstructor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.content.ContentConverter
 * @author: Tony Shen
 * @date: 2020-10-09 01:33
 * @version: V1.0 <描述当前版本功能>
 */
class ContentConverter {

    private val defaultConverter = DefaultRequestModelConverter()

    fun convert(content: Content): Params {
        val params = content.params ?: params()

        if (content.map != null) {
            val mapParams = Params.from(content.map)
            params.addAll(mapParams)
        }

        if (content.model != null) {
            val converter = if (content.modelConverter != null) {
                content.modelConverter.primaryConstructor?.call()
            } else {
                defaultConverter
            }

            val modelParams = converter?.convert(content) ?: params()
            params.addAll(modelParams)
        }

        return params
    }
}