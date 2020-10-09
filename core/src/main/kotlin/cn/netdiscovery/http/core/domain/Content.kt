package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.converter.RequestModelConverter
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.Content
 * @author: Tony Shen
 * @date: 2020-10-05 11:15
 * @version: V1.0 <描述当前版本功能>
 */
data class Content(
        val name: String,
        val params: Params? = null,
        val map: Map<String, String>? = null,
        val model: Any? = null,
        val modelConverter: KClass<out RequestModelConverter>? = null
) {

    fun isEmpty(): Boolean = params == null && map == null && model == null

    fun isNotEmpty(): Boolean = !isEmpty()
}