package cn.netdiscovery.http.core.converter

import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.converter.ConverterManager
 * @author: Tony Shen
 * @date: 2020-10-13 15:41
 * @version: V1.0 <描述当前版本功能>
 */
object ConverterManager {

    private var converter: Converter? = null

    fun converter(converter: Converter) {
        ConverterManager.converter = converter
    }

    fun <T> fromJson(json: String, type: Type): T? = converter?.fromJson(json,type)

    fun toJson(data: Any): String? = converter?.toJson(data)
}