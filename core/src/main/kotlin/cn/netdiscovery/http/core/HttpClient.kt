package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.aop.ProcessorStore
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.storage.cookie.ClientCookieHandler
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.RequestMethod
import cn.netdiscovery.http.core.storage.Storage
import cn.netdiscovery.http.core.websocket.ReconnectWebSocketWrapper
import cn.netdiscovery.http.core.websocket.WSConfig
import okhttp3.*
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
     * Create post request
     * @url - part of baseUrl
     * @customUrl - full url which will be used instead baseUrl + url
     * @query - will build query in url "url?param1=val1&param2..."
     * @headers - http headers
     *
     * @see Call
     *
     * @return okhttp3.Call
     */
    fun get(url: String = "", customUrl: String? = null, query: Params? = null, headers: Params? = null): Call

    /**
     * Create post request
     * @url - part of baseUrl
     * @customUrl - full url which will be used instead baseUrl + url
     * @body - post body
     * @headers - http headers
     *
     * @see Params
     * @see Call
     *
     * @return okhttp3.Call
     */
    fun post(url: String = "", customUrl: String? = null, body: Params, headers: Params? = null): Call

    /**
     * @see #post
     * @see Params
     * @see Call
     */
    fun put(url: String = "", customUrl: String? = null, body: Params, headers: Params? = null): Call

    /**
     * @see #get
     * @see Params
     * @see Call
     */
    fun delete(url: String = "", customUrl: String? = null, query: Params, headers: Params? = null): Call

    fun head(url: String = "", customUrl: String? = null, query: Params, headers: Params? = null): Call

    fun patch(url: String = "", customUrl: String? = null, body: Params, headers: Params? = null): Call

    /**
     * @see #post
     * @see Params
     * @see Call
     *
     * @json - used instead bady in post request
     */
    fun jsonPost(url: String = "", customUrl: String? = null, json: String, headers: Params? = null): Call

    /**
     * @see #put and #jsonPost
     * @see Params
     * @see Call
     */
    fun jsonPut(url: String = "", customUrl: String? = null, json: String, headers: Params? = null): Call

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

    fun websocket(url:String,query: Params? = null, headers: Params? = null, listener: WebSocketListener, wsConfig: WSConfig): ReconnectWebSocketWrapper

    /**
     * @see ProcessorStore
     */
    fun getProcessorStore(): ProcessorStore

    /**
     * @see OkHttpClient
     */
    fun okHttpClient(): OkHttpClient

    /**
     * @see StorageProvider
     */
    fun getStorageProvider(): Storage
}