package cn.netdiscovery.http.core.domain.content

import cn.netdiscovery.http.core.request.converter.RequestContentConverter
import cn.netdiscovery.http.core.domain.Params
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.content.Content
 * @author: Tony Shen
 * @date: 2020-10-05 11:15
 * @version: V1.0 <描述当前版本功能>
 */
data class Content(
        val name: String,
        val params: Params? = null,
        val map: Map<String, String>? = null,
        val model: Any? = null,
        val modelConverter: KClass<out RequestContentConverter>? = null
) {

    fun isEmpty(): Boolean = params == null && map == null && model == null

    fun isNotEmpty(): Boolean = !isEmpty()
}