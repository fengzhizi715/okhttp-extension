package cn.netdiscovery.http.core.request.method

import cn.netdiscovery.http.core.config.resolvers
import cn.netdiscovery.http.core.requestBlock

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.request.method.AbstractRequestMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 00:22
 * @version: V1.0 <描述当前版本功能>
 */
abstract class AbstractRequestMethodProcessor<T : Any> : RequestMethodProcessor<T> {

    protected val contentModifications: Map<String, requestBlock> = resolvers

    protected fun getContentNames() = contentModifications.keys
}