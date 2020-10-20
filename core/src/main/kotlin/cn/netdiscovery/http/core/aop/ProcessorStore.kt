package cn.netdiscovery.http.core.aop

import cn.netdiscovery.http.core.RequestProcessor
import cn.netdiscovery.http.core.ResponseProcessor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.aop.ProcessorStore
 * @author: Tony Shen
 * @date: 2020-10-03 13:34
 * @version: V1.0 <描述当前版本功能>
 */
interface ProcessorStore {

    fun addRequestProcessor(requestProcessor: RequestProcessor)

    fun removeRequestProcessor(requestProcessor: RequestProcessor)

    fun getRequestProcessors(): List<RequestProcessor>

    fun addResponseProcessor(responseProcessor: ResponseProcessor)

    fun removeResponseProcessor(responseProcessor: ResponseProcessor)

    fun getResponseProcessors(): List<ResponseProcessor>
}