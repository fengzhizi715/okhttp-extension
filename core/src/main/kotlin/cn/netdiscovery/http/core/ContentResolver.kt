package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.domain.Content
import cn.netdiscovery.http.core.domain.JsonContent
import cn.netdiscovery.http.core.method.JsonPostMethod
import cn.netdiscovery.http.core.method.RequestMethod
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.ContentResolver
 * @author: Tony Shen
 * @date: 2020-10-08 01:22
 * @version: V1.0 <描述当前版本功能>
 */
interface ContentResolver {

    fun resolve(method: RequestMethod<out Any>): Content?
}

class ReflectiveContentResolver(private val nameContaining: String) : ContentResolver {

    override fun resolve(method: RequestMethod<out Any>): Content? {
        val methodClass = method::class
        val fields = methodClass.memberProperties.filter { it.name.contains(nameContaining) }

        if (fields.isEmpty()) return null

        val objects = fields.map { it.name to it.getter.call(method) }.toMap()

        val primaryConstructor = Content::class.primaryConstructor!!
        val params = primaryConstructor.parameters

        val objectNames = objects.keys
        val args = params.map { param ->
            if (param.name == "name") {
                nameContaining
            } else {
                val names = objectNames.filter {
                    it.contains(param.name ?: "", true)
                }

                if(names.isNotEmpty()) {
                    val name = names.first()
                    objects[name]
                } else {
                    null
                }
            }
        }.toTypedArray()

        return Content::class.primaryConstructor!!.call(*args)
    }
}

fun jsonContentResolver(method: JsonPostMethod<out Any>) = JsonContent(
        method.json,
        method.jsonModel,
        method.jsonConverter
)