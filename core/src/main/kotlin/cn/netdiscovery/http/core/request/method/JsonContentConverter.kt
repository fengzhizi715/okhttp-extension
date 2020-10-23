package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.domain.content.JsonContent
import cn.netdiscovery.http.core.exception.JsonConverterNotFoundException
import cn.netdiscovery.http.core.exception.RequestMethodException
import kotlin.reflect.full.primaryConstructor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.content.JsonContentConverter
 * @author: Tony Shen
 * @date: 2020-10-09 02:02
 * @version: V1.0 <描述当前版本功能>
 */
object JsonContentConverter {

    fun convert(content: JsonContent): String? {
        return when {
            content.json != null && content.jsonModel != null -> throw RequestMethodException("Cont apply json string and json model")
            content.jsonModel != null && content.jsonConverter == null -> throw JsonConverterNotFoundException("for model ${content.jsonModel}")
            content.jsonModel != null -> {
                val converter = content.jsonConverter?.primaryConstructor?.call() ?: throw JsonConverterNotFoundException("for model ${content.jsonModel}")
                converter.convert(content.jsonModel!!)
            }
            content.json != null -> content.json!!
            else -> null
        }
    }
}