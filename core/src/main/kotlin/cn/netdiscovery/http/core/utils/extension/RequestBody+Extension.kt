package cn.netdiscovery.http.core.utils.extension

import okhttp3.RequestBody
import okio.Buffer

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.`RequestBody+Extension`
 * @author: Tony Shen
 * @date: 2021/11/17 3:46 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> RequestBody.flatMap(transform: (String) -> T): T = transform(
    Buffer().use {
        writeTo(it)
        it.readUtf8()
    }
)
