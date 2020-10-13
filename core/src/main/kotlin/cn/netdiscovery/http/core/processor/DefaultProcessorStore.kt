package cn.netdiscovery.http.core.processor

import cn.netdiscovery.http.core.request.RequestProcessor
import cn.netdiscovery.http.core.processor.response.ResponseProcessor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.DefaultProcessorStore
 * @author: Tony Shen
 * @date: 2020-10-03 13:48
 * @version: V1.0 <描述当前版本功能>
 */
class DefaultProcessorStore(private val requestProcessors: MutableList<RequestProcessor>,
                            private val responseProcessors: MutableList<ResponseProcessor>) : ProcessorStore {

    override fun registerRequestProcessor(requestProcessor: RequestProcessor) {
        requestProcessors.add(requestProcessor)
    }

    override fun removeRequestProcessor(requestProcessor: RequestProcessor) {
        requestProcessors.remove(requestProcessor)
    }

    override fun getRequestProcessors(): List<RequestProcessor> = requestProcessors

    override fun registerResponseProcessor(responseProcessor: ResponseProcessor) {
        responseProcessors.add(responseProcessor)
    }

    override fun removeResponseProcessor(responseProcessor: ResponseProcessor) {
        responseProcessors.remove(responseProcessor)
    }

    override fun getResponseProcessors(): List<ResponseProcessor> = responseProcessors
}