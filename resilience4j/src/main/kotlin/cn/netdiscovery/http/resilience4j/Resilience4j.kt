package cn.netdiscovery.http.resilience4j

import io.github.resilience4j.bulkhead.Bulkhead
import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.timelimiter.TimeLimiter
import io.vavr.control.Try
import okhttp3.Response
import java.util.concurrent.Callable
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
typealias OnAction = () -> Response

typealias OnFuture = () -> CompletableFuture<Response>

typealias OnFallback = (Throwable)-> Response

typealias IsError = (Response) -> Boolean

typealias OnError = (Throwable) -> Unit

object Resilience4j {

    operator fun invoke(circuitBreaker: CircuitBreaker = CircuitBreaker.ofDefaults("circuitBreaker"),
           timeLimiter: TimeLimiter = TimeLimiter.ofDefaults("timeLimiter"),
           onFuture: OnFuture,
           onFallback: OnFallback
    ):Response {
        val restrictedCall: Callable<Response> = TimeLimiter.decorateFutureSupplier(timeLimiter) {
            onFuture()
        }

        val chainedCallable: Callable<Response> = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall)

        val result: Try<Response> = Try.of{ chainedCallable.call() }
            .recover { throwable ->
                onFallback(throwable)
            }

        return result.get()
    }

    /**
     * 断路器
     */
    object CircuitBreak {
        private object CircuitError : RuntimeException()

        operator fun invoke(
            circuitBreaker: CircuitBreaker = CircuitBreaker.ofDefaults("circuitBreaker"),
            onAction: OnAction,
            isError: IsError = { it.code != 200 },
            onError: OnError
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
            onFuture: OnFuture,
            isError: IsError = { it.code != 200 },
            onError: OnError
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
    object TimeLimit {

        operator fun invoke(
            timeLimiter: TimeLimiter = TimeLimiter.ofDefaults("timeLimiter"),
            onAction: OnAction,
            onError: OnError
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
            onFuture: OnFuture,
            onError: OnError
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

    object RateLimit {

        operator fun invoke(
            rateLimit: RateLimiter = RateLimiter.ofDefaults("rateLimiter"),
            onAction: OnAction,
            onError: OnError
        ): Response? {
            try {
                return rateLimit.executeCallable {
                    onAction()
                }
            } catch (e: Exception) {
                onError(e)
            }

            return null
        }
    }

    object RetryFailures {

        private class RetryError(val response: Response) : RuntimeException()

        operator fun invoke(
            retry: Retry = Retry.ofDefaults("retryFailures"),
            onAction: OnAction,
            isError: IsError = { it.code != 200 },
            onError: OnError
        ): Response {
            return try {
                retry.executeCallable {
                    onAction().apply {
                        if (isError(this))
                            throw RetryError(this)
                    }
                }
            } catch (e: RetryError) {
                onError(e)
                e.response
            }
        }
    }

    object Bulkheading {

        operator fun invoke(
            bulkhead: Bulkhead = Bulkhead.ofDefaults("bulkhead"),
            onAction: OnAction,
            onError: OnError
        ): Response? {
            try {
                return bulkhead.executeCallable {
                    onAction()
                }
            } catch (e: BulkheadFullException) {
                onError(e)
            }

            return null
        }
    }
}