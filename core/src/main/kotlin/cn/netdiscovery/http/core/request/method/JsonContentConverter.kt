package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.domain.content.JsonContent
import cn.netdiscovery.http.core.exception.JsonConverterNotFoundException
import cn.netdiscovery.http.core.exception.RequestMethodException
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.utils.cache
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

    fun convert(content: JsonContent): String? = when {
        content.json != null && content.jsonModel != null          -> throw RequestMethodException("Can not apply json string and json model")
        content.jsonModel != null && content.jsonConverter == null -> throw JsonConverterNotFoundException("for model ${content.jsonModel}")
        content.jsonModel != null                                  -> {
            val converter =
                if (cache[content.jsonConverter] != null) {
                    cache[content.jsonConverter] as RequestJSONConverter
                } else {
                    val tempConverter = content.jsonConverter?.primaryConstructor?.call()
                        ?: throw JsonConverterNotFoundException("for model ${content.jsonModel}")
                    cache[content.jsonConverter!!] = tempConverter!!
                    tempConverter
                }

            converter.convert(content.jsonModel!!) // 将 jsonModel 转换成 String 格式
        }
        content.json != null                                       -> content.json
        else                                                       -> null
    }
}