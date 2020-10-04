package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.processor.method.RequestMethodProcessor

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.ProcessResult
 * @author: Tony Shen
 * @date: 2020-10-04 13:31
 * @version: V1.0 <描述当前版本功能>
 */
class ProcessResult <T : Any>(private val methodProcessor: RequestMethodProcessor<out T>) {

}