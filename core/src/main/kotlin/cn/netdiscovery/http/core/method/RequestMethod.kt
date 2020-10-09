package cn.netdiscovery.http.core.method

import cn.netdiscovery.http.core.Params
import cn.netdiscovery.http.core.response.ResponseMapper
import cn.netdiscovery.http.core.converter.RequestJsonConverter
import cn.netdiscovery.http.core.converter.RequestModelConverter
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.method.RequestMethod
 * @author: Tony Shen
 * @date: 2020-10-03 01:27
 * @version: V1.0 <描述当前版本功能>
 */
open class RequestMethod<T> protected constructor() {
    var url: String? = null
    var customUrl: String? = null
    var dateTimePattern: String = "yyyy-MM-dd hh:mm"
    var parallel: Boolean = false
    var isCountableEnabled: Boolean = true

    // url "localhost?myquery=myvalue"
    var queriesParams: Params? = null
    var queriesMap: Map<String, String>? = null
    var queriesModel: Any? = null
//    var queriesCountable: Countable? = null
    var queriesModelConverter: KClass<out RequestModelConverter>? = null

    // url "localhost/{path_name}"
    var pathParams: Params? = null
    var pathMap: Map<String, String>? = null
    var pathModel: Any? = null
//    var pathCountable: Countable? = null
    var pathModelConverter: KClass<out RequestModelConverter>? = null

    var headersParams: Params? = null
    var headersMap: Map<String, String>? = null
    var headersModel: Any? = null
//    var headersCountable: Countable? = null
    var headersModelConverter: KClass<out RequestModelConverter>? = null

    var responseMapper: KClass<out ResponseMapper<*>>? = null
}

class GetMethod<T> : RequestMethod<T>()

class PostMethod<T> : RequestMethod<T>() {
    var bodyParams: Params? = null
    var bodyMap: Map<String, String>? = null
    var bodyModel: Any? = null
//    var bodyCountable: Countable? = null
    var bodyModelConverter: KClass<out RequestModelConverter>? = null
}

class PutMethod<T>: RequestMethod<T>() {
    var bodyParams: Params? = null
    var bodyMap: Map<String, String>? = null
    var bodyModel: Any? = null
//    var bodyCountable: Countable? = null
    var bodyModelConverter: KClass<out RequestModelConverter>? = null
}

class DeleteMethod<T>: RequestMethod<T>()

class JsonPostMethod<T> : RequestMethod<T>() {
    var json: String? = null
    var jsonModel: Any? = null
    var jsonConverter: KClass<out RequestJsonConverter>? = null
}

class JsonPutMethod<T>: RequestMethod<T>() {
    var json: String? = null
    var jsonModel: Any? = null
    var jsonConverter: KClass<out RequestJsonConverter>? = null
}