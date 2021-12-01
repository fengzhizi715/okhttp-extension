package cn.netdiscovery.http.core.test.circuitbreak.resilience4j

import cn.netdiscovery.http.core.test.okhttpextension.apiService
import cn.netdiscovery.http.resilience4j.Resilience4j

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.circuitbreak.resilience4j.TestRetryFailures
 * @author: Tony Shen
 * @date: 2021/12/1 7:58 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    Resilience4j.RetryFailures.invoke(onAction = {
        apiService.testGet("Tony").sync()
    }, onError = {
        println(it.message)
    })
}