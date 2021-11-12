package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.response.ResponseMapper
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.request.converter.RequestContentConverter
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.RequestMethod
 * @author: Tony Shen
 * @date: 2020-10-03 01:27
 * @version: V1.0 <描述当前版本功能>
 */
sealed class RequestMethod<T> {
    var url: String? = null
    var customUrl: String? = null
    var dateTimePattern: String = "yyyy-MM-dd hh:mm"
    var parallel: Boolean = false

    var queriesParams: Params? = null
    var queriesMap: Map<String, String>? = null
    var queriesModel: Any? = null
    var queriesModelConverter: KClass<out RequestContentConverter>? = null

    var pathParams: Params? = null
    var pathMap: Map<String, String>? = null
    var pathModel: Any? = null
    var pathModelConverter: KClass<out RequestContentConverter>? = null

    var headersParams: Params? = null
    var headersMap: Map<String, String>? = null
    var headersModel: Any? = null
    var headersModelConverter: KClass<out RequestContentConverter>? = null

    var responseMapper: KClass<out ResponseMapper<*>>? = null
}

class GetMethod<T> : RequestMethod<T>()

open class PostMethod<T> : RequestMethod<T>() {
    var bodyParams: Params? = null
    var bodyMap: Map<String, String>? = null
    var bodyModel: Any? = null
    var bodyModelConverter: KClass<out RequestContentConverter>? = null
}

class PutMethod<T>: PostMethod<T>()

class DeleteMethod<T>: RequestMethod<T>()

class HeadMethod<T>: RequestMethod<T>()

class PatchMethod<T>: PostMethod<T>()

open class JsonPostMethod<T>: RequestMethod<T>() {
    var json: String? = null
    var jsonModel: Any? = null
    var jsonConverter: KClass<out RequestJSONConverter>? = null
}

class JsonPutMethod<T>: JsonPostMethod<T>()