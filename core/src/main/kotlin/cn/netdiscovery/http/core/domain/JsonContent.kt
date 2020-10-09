package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.converter.RequestJsonConverter
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.JsonContent
 * @author: Tony Shen
 * @date: 2020-10-08 01:11
 * @version: V1.0 <描述当前版本功能>
 */
data class JsonContent(
        var json: String? = null,
        var jsonModel: Any? = null,
        var jsonConverter: KClass<out RequestJsonConverter>? = null
)