package cn.netdiscovery.http.core.request.converter

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.content.Content
import cn.netdiscovery.http.core.domain.params
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.converter.DefaultRequestContentConverter
 * @author: Tony Shen
 * @date: 2020-10-09 01:39
 * @version: V1.0 <描述当前版本功能>
 */
class DefaultRequestContentConverter(dateTimePattern: String = "yyyy-MM-dd hh:mm") : RequestContentConverter {

    private val formatter = DateTimeFormatter.ofPattern(dateTimePattern)

    override fun convert(content: Content): Params {
        val model = content.model ?: return params()
        val properties = model::class.declaredMemberProperties

        val paramPairs = properties.mapNotNull { property ->
            if (isCollection(property))
                return@mapNotNull null

            val name = property.name

            val value = property.getter.call(model)
            val stringValue = resolveString(value)

            name to stringValue
        }

        val collectionsParams = properties.filter { isCollection(it) }
                .flatMap { property ->
                    val name = property.name

                    val collection = resolveCollection(property, model)
                    collection.map {
                        name to resolveString(it)
                    }
                }

        return params(*paramPairs.toTypedArray()).apply {
            addAll(*collectionsParams.toTypedArray())
        }
    }

    private fun resolveString(obj: Any?): String = when {
        obj == null -> ""
        String::class.java.isAssignableFrom(obj::class.java) -> obj as String
        TemporalAccessor::class.java.isAssignableFrom(obj::class.java) -> formatter.format(obj as TemporalAccessor)
        else -> obj.toString()
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
}