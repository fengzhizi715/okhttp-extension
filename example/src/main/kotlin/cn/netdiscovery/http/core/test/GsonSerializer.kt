package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.serializer.Serializer
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.GsonSerializer
 * @author: Tony Shen
 * @date: 2020-10-13 16:10
 * @version: V1.0 <描述当前版本功能>
 */
class GsonSerializer: Serializer {

    override fun <T> fromJson(json: String, type: Type): T = gson.fromJson(json,type)

    override fun toJson(data: Any): String = gson.toJson(data)
}