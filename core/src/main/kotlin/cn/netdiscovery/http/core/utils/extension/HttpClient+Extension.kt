package cn.netdiscovery.http.core.utils.extension

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.dsl.context.HttpGetContext
import okhttp3.Response
import java.util.concurrent.CompletableFuture

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.utils.extension.`HttpClient+Extension`
 * @author: Tony Shen
 * @date: 2021/11/17 1:27 上午
 * @version: V1.0 <描述当前版本功能>
 */
fun HttpClient.asyncGet(init: HttpGetContext.() -> Unit): CompletableFuture<Response> = okHttpClient().asyncCall(HttpGetContext().apply(init).buildRequest(this.getBaseUrl()))