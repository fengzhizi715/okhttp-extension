package cn.netdiscovery.http.core.interceptors

import cn.netdiscovery.http.core.utils.extension.flatMap
import okhttp3.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.CurlLoggingInterceptor
 * @author: Tony Shen
 * @date: 2021/11/17 3:45 下午
 * @version: V1.0 将网络请求转换成 curl 的命令并打印，便于调试
 * 在生产环境下，可以考虑关闭。
 */
class CurlLoggingInterceptor(
    private val log: (String) -> Unit = ::println
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        log("curl:")
        log("╔══════════════════════════════════════════════════════════════════════════════════════════════════")
        val command = buildCurlCommand(request)
        log("║ $command")
        log("╚══════════════════════════════════════════════════════════════════════════════════════════════════")

        return chain.proceed(request)
    }

    private fun buildCurlCommand(request: Request) = buildString {
        append("curl -X ${request.method}")
        append(buildCurlHeaderOption(request.headers))
        append(buildBodyOption(request.body))
        append(""" "${request.url}"""")
    }

    private fun buildCurlHeaderOption(headers: Headers): String {
        return headers.asSequence().map { (name, value) ->
            val trimmedValue = value.trimDoubleQuote()
            """ -H "$name: $trimmedValue""""
        }.joinToString("")
    }

    private fun buildBodyOption(body: RequestBody?): String? {
        if (body == null) return ""

        return if (body is MultipartBody) {
            body.parts?.joinToString(separator=" ", transform = {

                val map = mutableMapOf<String,String>()

                it.headers?.get("Content-Disposition")?.split(";")?.filter {
                    it.contains("=")
                }?.forEach {
                    val array = it.split("=")
                    map[array[0].trim()]=array[1].trim()
                }

                " -F ${map["name"]}=@${map["filename"]}"
            })
        } else {
            body.flatMap {
                it.replace("\n", "\\n")
                    .replace("\r", "\\r")
            }.run {
                " -d '$this'"
            }
        }
    }
}

private fun String.trimDoubleQuote() =
    if (startsWith('"') && endsWith('"')) {
        substring(1, length - 1)
    } else this
