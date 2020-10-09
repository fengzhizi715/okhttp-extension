package cn.netdiscovery.http.core.processor.method

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.response.ResponseConsumer
import cn.netdiscovery.http.core.converter.ContentConverter
import cn.netdiscovery.http.core.converter.JsonContentConverter
import cn.netdiscovery.http.core.domain.Content
import cn.netdiscovery.http.core.domain.JsonContent
import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.processor.params.ParamsProcessor
import cn.netdiscovery.http.core.processor.params.SingleParamsProcessor
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.method.SingleMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 01:28
 * @version: V1.0 <描述当前版本功能>
 */
class SingleMethodProcessor<T : Any>(
        private val method: RequestMethod<*>,
        private val client: HttpClient,
        private val contents: List<Content>,
        private val jsonContent: JsonContent?
) : AbstractRequestMethodProcessor<T>() {

    private val contentConverter = ContentConverter()

    private val jsonConverter by lazy { JsonContentConverter() }

    private lateinit var paramsProcessor: ParamsProcessor<T>

    override fun process(): ResponseConsumer<T> {
        val params = createParams()
        paramsProcessor = SingleParamsProcessor(method, client, contentModifications)
        return paramsProcessor.process(params)
    }

    override fun processAsync(): CompletableFuture<ResponseConsumer<T>> {
        val params = createParams()
        paramsProcessor = SingleParamsProcessor(method, client, contentModifications)
        return paramsProcessor.processAsync(params)
    }

    private fun createParams(): Map<String, Params> {
        return contents.map { it.name to it }
                .map { it.first to contentConverter.convert(it.second) }
                .toMap()
                .let {
                    if(jsonContent != null) {
                        injectJsonParam(it.toMutableMap())
                    } else it
                }
    }

    private fun injectJsonParam(namedParams: MutableMap<String, Params>): Map<String, Params> {
        val json = jsonConverter.convert(jsonContent!!) ?: ""
        namedParams["json"] = params("" to json)
        return namedParams
    }
}