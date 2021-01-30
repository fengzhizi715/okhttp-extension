package cn.netdiscovery.http.core.test.circuitbreak.resilience4j

import cn.netdiscovery.http.core.test.okhttpextension.apiService
import cn.netdiscovery.http.resilience4j.Resilience4j
import io.github.resilience4j.timelimiter.TimeLimiter

import io.github.resilience4j.timelimiter.TimeLimiterRegistry

import io.github.resilience4j.timelimiter.TimeLimiterConfig
import java.time.Duration


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.circuitbreak.resilience4j.TestTimeLimite
 * @author: Tony Shen
 * @date: 2021-01-30 18:56
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {
    val config = TimeLimiterConfig.custom()
        .cancelRunningFuture(true)
        .timeoutDuration(Duration.ofMillis(1000))
        .build()

    // 使用自定义的全局配置创建一个TimeLimiterRegistry
    val timeLimiterRegistry = TimeLimiterRegistry.of(config)

    //registry使用默认的配置创建一个TimeLimiter
    val timeLimiterWithDefaultConfig: TimeLimiter = timeLimiterRegistry.timeLimiter("name1")

    Resilience4j.TimeLimite.invoke(timeLimiterWithDefaultConfig, {
        apiService.testGet("Tony").async()
    },{
        println(it.message)
    })
}