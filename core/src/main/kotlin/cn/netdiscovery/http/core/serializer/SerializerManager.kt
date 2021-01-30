package cn.netdiscovery.http.core.serializer

import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.serializer.SerializerManager
 * @author: Tony Shen
 * @date: 2020-10-13 15:41
 * @version: V1.0 序列化管理类
 */
object SerializerManager {

    private var serializer: Serializer? = null

    fun converter(serializer: Serializer) {
        SerializerManager.serializer = serializer
    }

    fun <T> fromJson(json: String, type: Type): T? = serializer?.fromJson(json,type)

    fun toJson(data: Any): String? = serializer?.toJson(data)
}