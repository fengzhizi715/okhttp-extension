package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.ProcessResult
import cn.netdiscovery.http.core.config.resolvers
import cn.netdiscovery.http.core.config.ua
import cn.netdiscovery.http.core.domain.JsonPostMethod
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.exception.ResponseMapperNotFoundException
import cn.netdiscovery.http.core.exception.UrlNotFoundException
import cn.netdiscovery.http.core.response.EmptyResponseMapper
import cn.netdiscovery.http.core.response.ResponseMapper
import cn.netdiscovery.http.core.response.StringResponseMapper
import okhttp3.Call
import okhttp3.Response
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.method.MethodBuilder
 * @author: Tony Shen
 * @date: 2020-10-05 01:25
 * @version: V1.0 <描述当前版本功能>
 */
class MethodBuilder<T: Any>(private val client: HttpClient, private val type: KClass<out T>) {

    private var responseMapper: KClass<out ResponseMapper<*>>? = null

    fun build(method: RequestMethod<T>): ProcessResult<T> {
        method.url ?: throw UrlNotFoundException()

        responseMapper = method.responseMapper ?: getDefaultMapper(type.java)
        method.responseMapper = responseMapper

        if (!client.getUserAgent().isNullOrEmpty()) {
            method.headersParams?.let {
                it.add(Pair(ua,client.getUserAgent()))
            }?: kotlin.run {
                method.headersParams = Params.of(Pair(ua,client.getUserAgent()))
            }
        }

        val contents = resolvers.map { ReflectiveContentResolver(it.key).resolve(method) }.filterNotNull()

        val jsonContent = if (method is JsonPostMethod<T>) {
            jsonContentResolver(method)
        } else null

        val methodProcessor:RequestMethodProcessor<T> = MethodProcessor(method, client, contents.toList(), jsonContent)
        return ProcessResult(methodProcessor)
    }

    private fun getDefaultMapper(genericType: Class<out Any>): KClass<out ResponseMapper<*>>? = when {
        Response::class.java.isAssignableFrom(genericType) -> EmptyResponseMapper::class
        String::class.java.isAssignableFrom(genericType)   -> StringResponseMapper::class
        Call::class.java.isAssignableFrom(genericType)     -> null
        else                                               -> throw ResponseMapperNotFoundException()
    }
}