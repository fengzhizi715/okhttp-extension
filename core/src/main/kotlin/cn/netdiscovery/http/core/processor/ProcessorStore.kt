package cn.netdiscovery.http.core.processor

import cn.netdiscovery.http.core.processor.request.RequestProcessor
import cn.netdiscovery.http.core.processor.response.ResponseProcessor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.ProcessorStore
 * @author: Tony Shen
 * @date: 2020-10-03 13:34
 * @version: V1.0 <描述当前版本功能>
 */
interface ProcessorStore {

    fun registerRequestProcessor(requestProcessor: RequestProcessor)

    fun removeRequestProcessor(requestProcessor: RequestProcessor)

    fun getRequestProcessors(): List<RequestProcessor>

    fun registerResponseProcessor(responseProcessor: ResponseProcessor)

    fun removeResponseProcessor(responseProcessor: ResponseProcessor)

    fun getResponseProcessors(): List<ResponseProcessor>
}