package cn.netdiscovery.http.extension.coroutines

import cn.netdiscovery.http.core.ProcessResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.coroutines.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-12-11 15:56
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.asFlow(): Flow<T> = flowOf(sync() as T)