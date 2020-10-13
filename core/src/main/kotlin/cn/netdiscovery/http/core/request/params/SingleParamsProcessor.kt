package cn.netdiscovery.http.core.request.params

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.ResponseConsumer
import cn.netdiscovery.http.core.domain.RequestMethodModel
import cn.netdiscovery.http.core.extension.executeAsync
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.requestBlock
import okhttp3.Request
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.params.SingleParamsProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 11:28
 * @version: V1.0 <描述当前版本功能>
 */
class SingleParamsProcessor<T>(
        private val method: RequestMethod<*>,
        private val client: HttpClient,
        private val modifications: Map<String, requestBlock>
) : ParamsProcessor<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun process(namedParams: Map<String, Params>): ResponseConsumer<T> {
        val methodModel = createMethodModel(namedParams)
        val call = client.processAndSend(methodModel.build())

        return applyMapper(method.responseMapper, call)
    }

    override fun processAsync(namedParams: Map<String, Params>): CompletableFuture<ResponseConsumer<T>> {
        val methodModel = createMethodModel(namedParams)
        val call = client.processAndSend(methodModel.build())

        return call.executeAsync()
                .thenApply {
                    applyMapper(method.responseMapper, it)
                }
    }

    private fun createMethodModel(namedParams: Map<String, Params>): RequestMethodModel {
        var methodModel = RequestMethodModel(requestBuilder = Request.Builder(),
                apiUrl = method.url,
                customUrl = method.customUrl,
                baseUrl = client.getBaseUrl())

        methodModel.setMethod(method)

        modifications.forEach { (name, block) ->
            if (namedParams.containsKey(name))
                methodModel = block(namedParams[name]!!, methodModel)
        }
        return methodModel
    }
}