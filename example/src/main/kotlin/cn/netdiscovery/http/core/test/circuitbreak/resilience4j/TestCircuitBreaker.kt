package cn.netdiscovery.http.core.test.circuitbreak.resilience4j

import cn.netdiscovery.http.core.test.okhttpextension.apiService
import cn.netdiscovery.http.resilience4j.Resilience4j
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.circuitbreak.resilience4j.TestCircuitBreaker
 * @author: Tony Shen
 * @date: 2021-01-30 20:42
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {
    // 创建断路器的自定义配置
    val circuitBreakerConfig = CircuitBreakerConfig.custom()
        .recordExceptions(IOException::class.java, TimeoutException::class.java)
        .build()

    val customCircuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig)

    Resilience4j.CircuitBreak.invoke(customCircuitBreaker, onAction = {
        apiService.testGet("Tony").sync()
    }, onError = {
        println(it.message)
    })
}