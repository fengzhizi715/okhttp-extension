package cn.netdiscovery.http.extension.result

import cn.netdiscovery.http.core.ProcessResult
import cn.netdiscovery.result.Result
import cn.netdiscovery.result.resultFrom

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.extension.result.`ProcessResult+Extension`
 * @author: Tony Shen
 * @date: 2020-12-11 11:54
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> ProcessResult<out Any>.getResult():Result<T,Exception> = resultFrom {
    async().get() as T
}