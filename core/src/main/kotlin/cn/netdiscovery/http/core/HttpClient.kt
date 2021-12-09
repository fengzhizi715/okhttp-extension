package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.aop.ProcessorStore
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.storage.cookie.ClientCookieHandler
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.dsl.context.*
import cn.netdiscovery.http.core.preconnetion.PreConnectCallback
import cn.netdiscovery.http.core.storage.Storage
import cn.netdiscovery.http.core.websocket.ReconnectWebSocketWrapper
import cn.netdiscovery.http.core.websocket.WSConfig
import okhttp3.*
import java.io.File
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.HttpClient
 * @author: Tony Shen
 * @date: 2020-09-20 13:18
 * @version: V1.0 HttpClient 接口，包含了 httpClient 提供的各种方法
 */
interface HttpClient {

    fun getBaseUrl(): String

    fun userAgent(agent: String): HttpClient

    fun getUserAgent(): String

    /**
     * 设置全局的 RequestJSONConverter
     */
    fun jsonConverter(jsonConverterClass: KClass<out RequestJSONConverter>): HttpClient

    /**
     * 全局的 RequestJSONConverter
     */
    fun getJsonConverter(): KClass<out RequestJSONConverter>?

    /**
     * @see ClientCookieHandler
     */
    fun getClientCookieHandler(): ClientCookieHandler?

    /**
     * 设置预连接
     */
    fun preConnection(url:String, callback: PreConnectCallback): OkHttpClient

    /**
     * Create post request
     * @url - part of baseUrl
     * @customUrl - full url which will be used instead baseUrl + url
     * @query - will build query in url "url?param1=val1&param2..."
     * @headers - http headers
     *
     * @see Response
     *
     * @return okhttp3.Response
     */
    fun get(url: String = "", customUrl: String? = null, query: Params? = null, headers: Params? = null): Response

    fun get(init: HttpGetContext.() -> Unit): Response

    /**
     * Create post request
     * @url - part of baseUrl
     * @customUrl - full url which will be used instead baseUrl + url
     * @query - will build query in url "url?param1=val1&param2..."
     * @body - post body
     * @headers - http headers
     *
     * @see Params
     * @see Response
     *
     * @return okhttp3.Response
     */
    fun post(url: String = "", customUrl: String? = null, query: Params? = null, body: Params, headers: Params? = null): Response

    fun post(init: HttpPostContext.() -> Unit): Response

    fun upload(url: String = "", customUrl: String? = null, query: Params? = null, headers: Params? = null, name: String, file: File): Response

    /**
     * @see #post
     * @see Params
     * @see Response
     */
    fun put(url: String = "", customUrl: String? = null, query: Params? = null, body: Params, headers: Params? = null): Response

    fun put(init: HttpPutContext.() -> Unit): Response

    /**
     * @see #get
     * @see Params
     * @see Response
     */
    fun delete(url: String = "", customUrl: String? = null, query: Params? = null, headers: Params? = null): Response

    fun delete(init:  HttpDeleteContext.() -> Unit): Response

    fun head(url: String = "", customUrl: String? = null, query: Params? = null, headers: Params? = null): Response

    fun head(init: HttpHeadContext.() -> Unit): Response

    fun patch(url: String = "", customUrl: String? = null, query: Params? = null, body: Params, headers: Params? = null): Response

    fun patch(init: HttpPatchContext.() -> Unit): Response

    /**
     * @see #post
     * @see Params
     * @see Response
     *
     * @json - used instead bady in post request
     */
    fun jsonPost(url: String = "", customUrl: String? = null, json: String, query: Params? = null, headers: Params? = null): Response

    /**
     * @see #put and #jsonPost
     * @see Params
     * @see Response
     */
    fun jsonPut(url: String = "", customUrl: String? = null, json: String, query: Params? = null, headers: Params? = null): Response

    /**
     * Same as send but request will be processed by RequestProcessors
     *
     * @see Request
     * @see Call
     * @see RequestMethod
     */
    fun processAndSend(request: Request.Builder): Call

    /**
     * You can send okhttp.Request instead using defined methods
     * RequestProcessors will be avoided
     *
     * @see Request
     * @see Call
     *
     * @return okhttp3.Call
     */
    fun send(request: Request): Call

    fun <T: Any> send(clazz: KClass<T>, request: RequestMethod<T>): ProcessResult<T>

    /**
     * 支持 websocket
     * @param url
     * @customUrl - full url which will be used instead baseUrl + url
     * @param query
     * @param headers
     * @param listener
     * @param wsConfig
     */
    fun websocket(url: String = "", customUrl: String?, query: Params? = null, headers: Params? = null, listener: WebSocketListener, wsConfig: WSConfig): ReconnectWebSocketWrapper

    /**
     * @see ProcessorStore
     */
    fun getProcessorStore(): ProcessorStore

    /**
     * @see StorageProvider
     */
    fun getStorageProvider(): Storage

    /**
     * @see OkHttpClient
     */
    fun okHttpClient(): OkHttpClient
}