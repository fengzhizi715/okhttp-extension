package cn.netdiscovery.http.core.processor.method

import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethodModel
import cn.netdiscovery.http.core.method.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.method.AbstractRequestMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 00:22
 * @version: V1.0 <描述当前版本功能>
 */
abstract class AbstractRequestMethodProcessor<T : Any> : RequestMethodProcessor<T> {

    protected val contentModifications: Map<String, (Params, RequestMethodModel) -> RequestMethodModel> = mapOf(
            "queries" to ::processQuery,
            "path"    to ::processPath,
            "body"    to ::processBody,
            "headers" to ::processHeaders,
            "json"    to ::processJson
    )

    protected fun getContentNames() = contentModifications.keys
}