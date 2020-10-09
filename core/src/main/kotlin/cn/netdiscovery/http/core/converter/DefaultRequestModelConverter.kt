package cn.netdiscovery.http.core.converter

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.annotation.ParamName
import cn.netdiscovery.http.core.domain.Content
import cn.netdiscovery.http.core.domain.params
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.converter.DefaultRequestModelConverter
 * @author: Tony Shen
 * @date: 2020-10-09 01:39
 * @version: V1.0 <描述当前版本功能>
 */
class DefaultRequestModelConverter(
        dateTimePattern: String = "yyyy-MM-dd hh:mm") : RequestModelConverter {

    private val formatter = DateTimeFormatter.ofPattern(dateTimePattern)

    override fun convert(content: Content): Params {
        val model = content.model ?: return params()
        val properties = model::class.declaredMemberProperties

        val paramPairs = properties.mapNotNull { property ->
            if (isCollection(property))
                return@mapNotNull null

            val name = getName(property)

            val value = property.getter.call(model)
            val stringValue = resolveString(value)

            name to stringValue
        }

        val collectionsParams = properties.filter { isCollection(it) }
                .flatMap { property ->
                    val name = getName(property)

                    val collection = resolveCollection(property, model)
                    collection.map {
                        name to resolveString(it)
                    }
                }

        val params = params(*paramPairs.toTypedArray())
        params.addAll(*collectionsParams.toTypedArray())

        return params
    }

    private fun resolveString(obj: Any?): String {
        return when {
            obj == null -> ""
            String::class.java.isAssignableFrom(obj::class.java) -> obj as String
            TemporalAccessor::class.java.isAssignableFrom(obj::class.java) -> formatter.format(obj as TemporalAccessor)
            else -> obj.toString()
        }
    }

    private fun resolveCollection(property: KProperty1<*, Any?>, model: Any): Collection<Any?> {
        val obj = property.getter.call(model)
        obj ?: return emptyList()
        return obj as Collection<Any?>
    }

    private fun isCollection(property: KProperty1<*, Any?>): Boolean {
        val javaField = property.javaField
        val type = javaField?.type ?: return false
        return Collection::class.java.isAssignableFrom(type)
    }

    private fun getName(property: KProperty1<*, Any?>): String {
        val javaField = property.javaField
        return if (javaField?.isAnnotationPresent(ParamName::class.java) == true) {
            javaField.getAnnotation(ParamName::class.java).name
        } else {
            property.name
        }
    }
}