package cn.netdiscovery.http.resilience4j

import io.github.resilience4j.timelimiter.TimeLimiter
import okhttp3.Response
import java.lang.Exception
import java.util.concurrent.CompletableFuture

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
     * 限时器
     */
    object TimeLimite {

        operator fun invoke(
            timeLimiter: TimeLimiter = TimeLimiter.ofDefaults("timeLimiter"),
            onAction: () -> CompletableFuture<Response>,
            onError:  (Throwable) -> Unit) {

            try {
                timeLimiter.executeFutureSupplier {
                    CompletableFuture.supplyAsync{
                        onAction()
                    }
                }
            } catch (e:Exception) {
                onError(e)
            }
        }
    }
}