package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.converter.Converter
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.GsonConverter
 * @author: Tony Shen
 * @date: 2020-10-13 16:10
 * @version: V1.0 <描述当前版本功能>
 */
class GsonConverter: Converter {

    override fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json,type)
    }

    override fun toJson(data: Any): String {
        return gson.toJson(data)
    }

}