package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.aop.ProcessorStore
import cn.netdiscovery.http.core.config.jsonMediaType
import cn.netdiscovery.http.core.config.ua
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.dsl.context.*
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.request.method.MethodBuilder
import cn.netdiscovery.http.core.storage.DefaultStorage
import cn.netdiscovery.http.core.storage.Storage
import cn.netdiscovery.http.core.storage.cookie.ClientCookieHandler
import cn.netdiscovery.http.core.storage.cookie.DefaultClientCookieHandler
import cn.netdiscovery.http.core.utils.extension.call
import cn.netdiscovery.http.core.websocket.ReconnectWebSocketWrapper
import cn.netdiscovery.http.core.websocket.WSConfig
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.CookieManager
import java.net.URI
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.OkHttpClientWrapper
 * @author: Tony Shen
 * @date: 2020-09-20 01:02
 * @version: V1.0 OkHttpClient 的包装类
 */
class OkHttpClientWrapper(private var baseUrl: String,
                          private val okHttpClient: OkHttpClient,
                          private val processorStore: ProcessorStore,
                          private val cookieManager: CookieManager?,
                          private val storageProvider: Storage = DefaultStorage(),
                          private val cookiesFileName: String?) : HttpClient {

    private var cookieHandler: ClientCookieHandler? = null
    private var userAgent = ""
    private var jsonConverterClass:KClass<out RequestJSONConverter>? = null

    init {
        if (cookieManager != null) {
            if (cookieHandler == null)
                cookieHandler = DefaultClientCookieHandler(this, cookieManager, storageProvider, cookiesFileName)

            cookiesFileName?.let {
                cookieHandler!!.restoreCookies(it).forEach {
                    cookieManager.cookieStore.add(URI.create(baseUrl), it)
                }
            }
        }
    }

    override fun getBaseUrl(): String = baseUrl

    override fun userAgent(userAgent: String): HttpClient {
        this.userAgent = userAgent
        return this
    }

    override fun getUserAgent(): String = userAgent

    override fun jsonConverter(jsonConverterClass:KClass<out RequestJSONConverter>): HttpClient {
        this.jsonConverterClass = jsonConverterClass
        return this
    }

    override fun getJsonConverter(): KClass<out RequestJSONConverter>? = jsonConverterClass

    override fun getClientCookieHandler(): ClientCookieHandler? = cookieHandler

    override fun get(url: String, customUrl: String?, query: Params?, headers: Params?): Response = okHttpClient.call{
        createRequest(url, customUrl, query, headers) {
            it.get()
        }
    }

    override fun get(init: HttpGetContext.() -> Unit): Response = okHttpClient.call {
        val context = HttpGetContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun post(url: String, customUrl: String?, query: Params?, body: Params, headers: Params?): Response = okHttpClient.call {
        createBodyRequest(url, customUrl, body, headers) { builder, body ->
            builder.post(body)
        }
    }

    override fun post(init: HttpPostContext.() -> Unit): Response = okHttpClient.call{
        val context = HttpPostContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun upload(url: String, customUrl: String?, headers: Params?, name: String, file: File): Response = okHttpClient.call {

        val body = MultipartBody.Builder().addPart(MultipartBody.Part.createFormData(name, file.name, file.asRequestBody())).build()

        createMultipartBodyRequest(url, customUrl, headers,body) { builder, body ->
            builder.post(body)
        }
    }

    override fun put(url: String, customUrl: String?, body: Params, headers: Params?): Response = okHttpClient.call {
        createBodyRequest(url, customUrl, body, headers) { builder, body ->
            builder.put(body)
        }
    }

    override fun put(init:  HttpPutContext.() -> Unit): Response = okHttpClient.call {
        val context = HttpPutContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun delete(url: String, customUrl: String?, query: Params, headers: Params?): Response = okHttpClient.call{
        createRequest(url, customUrl, query, headers) {
            it.delete()
        }
    }

    override fun delete(init: HttpDeleteContext.() -> Unit): Response = okHttpClient.call {
        val context = HttpDeleteContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun head(url: String, customUrl: String?, query: Params, headers: Params?): Response = okHttpClient.call {
        createRequest(url,customUrl,query,headers) {
            it.head()
        }
    }

    override fun head(init: HttpHeadContext.() -> Unit): Response = okHttpClient.call{
        val context = HttpHeadContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun patch(url: String, customUrl: String?, body: Params, headers: Params?): Response = okHttpClient.call {
        createBodyRequest(url, customUrl, body, headers) { builder, body ->
            builder.patch(body)
        }
    }

    override fun patch(init: HttpPatchContext.() -> Unit): Response = okHttpClient.call{
        val context = HttpPatchContext().apply(init)
        context.buildRequest(baseUrl)
    }

    override fun jsonPost(url: String, customUrl: String?, json: String, headers: Params?): Response = okHttpClient.call {
        createJsonRequest(url, customUrl, json, headers) { builder, requestBody ->
            builder.post(requestBody)
        }
    }

    override fun jsonPut(url: String, customUrl: String?, json: String, headers: Params?): Response = okHttpClient.call {
        createJsonRequest(url, customUrl, json, headers) { builder, requestBody ->
            builder.put(requestBody)
        }
    }

    override fun processAndSend(request: Request.Builder): Call {
        var builder = request
        processorStore.getRequestProcessors()
                .forEach {
                    builder = it.invoke(this, builder)
                }

        return send(builder.build())
    }

    override fun send(request: Request) = okHttpClient.newCall(request)

    override fun <T : Any> send(clazz: KClass<T>, requestMethod: RequestMethod<T>): ProcessResult<T> = MethodBuilder(this, clazz).build(requestMethod)

    override fun websocket(url: String, customUrl: String?, query: Params?, headers: Params?, listener: WebSocketListener, wsConfig: WSConfig): ReconnectWebSocketWrapper {

        return ReconnectWebSocketWrapper(okHttpClient, createWebSocketRequest(url,customUrl,query,headers), listener, wsConfig)
    }

    override fun getProcessorStore() = processorStore

    override fun okHttpClient() = okHttpClient

    override fun getStorageProvider(): Storage = storageProvider

    /**
     * 创建 request 请求，适用于 GET、DELETE、HEAD
     */
    private fun createRequest(url: String,
                              customUrl: String?,
                              query: Params?,
                              header: Params?,
                              block: (Request.Builder) -> Unit): Request {
        val url = buildUrl(url,customUrl,query)

        var builder = Request.Builder().url(url)

        block.invoke(builder)

        header?.getParams()?.forEach { builder.addHeader(it.first, it.second) }

        if (userAgent.isNotEmpty()) {
            builder.addHeader(ua, userAgent)
        }

        processorStore.getRequestProcessors()
                .forEach {
                    builder = it.invoke(this, builder)
                }

        return builder.build()
    }

    /**
     * 创建 request 请求，适用于 POST、PUT
     */
    private fun createBodyRequest(url: String,
                                  customUrl: String?,
                                  query: Params?,
                                  body: Params,
                                  header: Params?,
                                  block: (Request.Builder, FormBody) -> Unit): Request {

        val url = buildUrl(url,customUrl,query)

        var builder = Request.Builder().url(url)

        header?.getParams()?.forEach { builder.addHeader(it.first, it.second) }

        if (userAgent.isNotEmpty()) {
            builder.addHeader(ua, userAgent)
        }

        val formBody = FormBody.Builder()

        body.getParams().forEach {
            formBody.add(it.first, it.second)
        }

        block.invoke(builder, formBody.build())

        processorStore.getRequestProcessors()
                .forEach {
                    builder = it.invoke(this, builder)
                }

        return builder.build()
    }

    /**
     * 创建 request 请求，适用于文件 upload
     */
    private fun createMultipartBodyRequest(url: String,
                                  customUrl: String?,
                                  header: Params?,
                                  body: MultipartBody,
                                  block: (Request.Builder, RequestBody) -> Unit): Request {

        var request = Request.Builder().url(customUrl ?: "$baseUrl$url")

        header?.getParams()?.forEach { request.addHeader(it.first, it.second) }

        if (userAgent.isNotEmpty()) {
            request.addHeader(ua, userAgent)
        }

        block.invoke(request, body)

        processorStore.getRequestProcessors()
            .forEach {
                request = it.invoke(this, request)
            }

        return request.build()
    }

    /**
     * 创建 request 请求，适用于 POST、PUT
     */
    private fun createJsonRequest(url: String,
                                  customUrl: String?,
                                  json: String,
                                  header: Params?,
                                  block: (Request.Builder, RequestBody) -> Unit): Request {

        var request = Request.Builder().url(customUrl ?: "$baseUrl$url")

        header?.getParams()?.forEach { request.addHeader(it.first, it.second) }

        if (userAgent.isNotEmpty()) {
            request.addHeader(ua, userAgent)
        }

        val requestBody = json.toRequestBody(jsonMediaType)

        block.invoke(request, requestBody)

        processorStore.getRequestProcessors()
                .forEach {
                    request = it.invoke(this, request)
                }

        return request.build()
    }

    /**
     * 创建 websocket 请求
     */
    private fun createWebSocketRequest(url: String, customUrl: String?, query: Params?, header: Params?):Request {
        val url = buildUrl(url,customUrl,query)

        var builder = Request.Builder().url(url)

        header?.getParams()?.forEach { builder.addHeader(it.first, it.second) }

        return builder.build()
    }

    /**
     * 构建 url
     */
    private fun buildUrl(url: String, customUrl: String?, query: Params?):String {
        val query = query?.joinToString("&") { "${it.first}=${it.second}" }

        return if (query != null) {
            val base = customUrl ?: "$baseUrl$url"
            "$base?$query"
        } else {
            customUrl ?: "$baseUrl$url"
        }
    }
}