package cn.netdiscovery.http.core.request.params

import cn.netdiscovery.http.core.Cancelable
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.ResponseConsumer
import cn.netdiscovery.http.core.response.ResponseMapper
import cn.netdiscovery.http.core.exception.ResponseMapperNotFoundException
import okhttp3.Call
import okhttp3.Response
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.param.ParamsProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 11:26
 * @version: V1.0 <描述当前版本功能>
 */
abstract class ParamsProcessor<T> : Cancelable {

    abstract fun process(namedParams: Map<String, Params>): ResponseConsumer<T>

    abstract fun processAsync(namedParams: Map<String, Params>): CompletableFuture<ResponseConsumer<T>>

    fun applyMapper(mapperClass: KClass<out ResponseMapper<*>>?, call: Call): ResponseConsumer<T> {
        return if (mapperClass != null) {
            val mapper = mapperClass.primaryConstructor?.call() ?: throw ResponseMapperNotFoundException()
            val responseModel = mapper.map(call.execute())
            ResponseConsumer(responseModel = responseModel as T)
        } else {
            ResponseConsumer(call = call)
        }
    }
    
    @Synchronized
    fun applyMapper(mapperClass: KClass<out ResponseMapper<*>>?, response: Response): ResponseConsumer<T> {
        return if (mapperClass != null) {
            val mapper = mapperClass.primaryConstructor?.call() ?: throw ResponseMapperNotFoundException()
            val responseModel = mapper.map(response)
            ResponseConsumer(responseModel = responseModel as T)
        } else {
            ResponseConsumer(response = response)
        }
    }
}