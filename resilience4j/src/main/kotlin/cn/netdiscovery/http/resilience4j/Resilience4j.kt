package cn.netdiscovery.http.resilience4j

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.timelimiter.TimeLimiter
import okhttp3.Response
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.resilience4j.Resilience4j
 * @author: Tony Shen
 * @date: 2021-01-30 16:22
 * @version: V1.0 <描述当前版本功能>
 */
object Resilience4j {

    /**
     * 断路器
     */
    object CircuitBreak {
        private object CircuitError : RuntimeException()

        operator fun invoke(
            circuitBreaker: CircuitBreaker = CircuitBreaker.ofDefaults("circuitBreaker"),
            onAction: () -> Response,
            isError: (Response) -> Boolean = { it.code != 200 },
            onError: (Throwable) -> Unit
        ):Response? {
            try {
                return circuitBreaker.executeCallable {
                    onAction().apply {
                        if (isError(this))
                            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, CircuitError)
                    }
                }
            } catch (e: CallNotPermittedException) {
                onError(e)
            }

            return null
        }

        operator fun invoke(
            circuitBreaker: CircuitBreaker = CircuitBreaker.ofDefaults("circuitBreaker"),
            onFuture: () -> CompletableFuture<Response>,
            isError: (Response) -> Boolean = { it.code != 200 },
            onError: (Throwable) -> Unit
        ):CompletableFuture<Response>? {
            try {
                return circuitBreaker.executeSupplier {
                    onFuture().apply {
                        if (isError(this.get()))
                            circuitBreaker.onError(0, TimeUnit.MILLISECONDS, CircuitError)
                    }
                }
            } catch (e: CallNotPermittedException) {
                onError(e)
            }

            return null
        }
    }

    /**
     * 限时器
     */
    object TimeLimite {

        operator fun invoke(
            timeLimiter: TimeLimiter = TimeLimiter.ofDefaults("timeLimiter"),
            onAction: () -> Response,
            onError: (Throwable) -> Unit
        ):Response? {

            try {
                return timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync{
                        onAction()
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }

            return null
        }

        operator fun invoke(
            timeLimiter: TimeLimiter = TimeLimiter.ofDefaults("timeLimiter"),
            onFuture: () -> CompletableFuture<Response>,
            onError: (Throwable) -> Unit
        ):CompletableFuture<Response>? {

            try {
                return timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync{
                        onFuture()
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }

            return null
        }
    }
}