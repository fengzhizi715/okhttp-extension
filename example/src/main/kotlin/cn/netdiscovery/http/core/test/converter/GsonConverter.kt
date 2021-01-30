package cn.netdiscovery.http.core.test.converter

import cn.netdiscovery.http.core.serializer.Serializer
import cn.netdiscovery.http.core.test.gson
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.converter.GsonConverter
 * @author: Tony Shen
 * @date: 2020-10-13 16:10
 * @version: V1.0 <描述当前版本功能>
 */
class GsonConverter: Serializer {

    override fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json,type)
    }

    override fun toJson(data: Any): String {
        return gson.toJson(data)
    }

}