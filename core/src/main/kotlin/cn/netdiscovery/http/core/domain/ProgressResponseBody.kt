package cn.netdiscovery.http.core.domain

import cn.netdiscovery.http.core.ProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.domain.ProgressResponseBody
 * @author: Tony Shen
 * @date: 2021/12/24 5:04 PM
 * @version: V1.0 <描述当前版本功能>
 */
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            var lastBytesRead: Long = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener(totalBytesRead, lastBytesRead, responseBody.contentLength(), bytesRead == -1L)
                lastBytesRead = totalBytesRead
                return bytesRead
            }
        }
    }
}