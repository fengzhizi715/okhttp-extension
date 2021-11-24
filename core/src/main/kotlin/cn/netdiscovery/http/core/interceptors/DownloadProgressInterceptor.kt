package cn.netdiscovery.http.core.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.DownloadProgressInterceptor
 * @author: Tony Shen
 * @date: 2021/11/24 4:09 下午
 * @version: V1.0 <描述当前版本功能>
 */
class DownloadProgressInterceptor(private val downloadProgressListener: DownloadProgressListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.body?.let {
            originalResponse.newBuilder()
                .body(DownloadProgressResponseBody(it, downloadProgressListener))
                .build()
        } ?: originalResponse
    }

    class DownloadProgressResponseBody(
        private val responseBody: ResponseBody,
        private val downloadProgressListener: DownloadProgressListener
    ) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null

        override fun contentType(): MediaType? = responseBody.contentType()

        override fun contentLength(): Long = responseBody.contentLength()

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody.source()).buffer()
            }

            return bufferedSource!!
        }

        private fun source(source: Source) = object : ForwardingSource(source) {
            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                downloadProgressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }
}

interface DownloadProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}