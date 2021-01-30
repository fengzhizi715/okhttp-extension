package cn.netdiscovery.http.core.serializer

import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.serializer.Serializer
 * @author: Tony Shen
 * @date: 2020-10-13 15:04
 * @version: V1.0 序列化接口
 */
interface Serializer {

    /**
     * 将字符串转换成 type 类型的对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    fun <T> fromJson(json: String, type: Type): T

    /**
     * 将对象序列化成字符串对象
     * @param data
     * @return
     */
    fun toJson(data: Any): String
}
