package cn.netdiscovery.http.core.utils.extension

import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.ProgressListener
import cn.netdiscovery.http.core.domain.ProgressResponseBody
import okhttp3.Response

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.file.`HttpClientBuilder+Extension`
 * @author: Tony Shen
 * @date: 2021/12/24 5:00 PM
 * @version: V1.0 <描述当前版本功能>
 */

/**
 * 下载需要进度信息的情况，最后单独使用一个 httpClient
 */
fun HttpClientBuilder.addProgressListener(listener: ProgressListener):HttpClientBuilder {
    this.addNetworkInterceptor { chain ->
        val originalResponse: Response = chain.proceed(chain.request())
        originalResponse.newBuilder()
            .body(originalResponse.body?.let { ProgressResponseBody(it, listener) })
            .build()
    }

    return this
}
