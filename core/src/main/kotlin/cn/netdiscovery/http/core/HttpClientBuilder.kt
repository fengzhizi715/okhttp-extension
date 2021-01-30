package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.aop.DefaultProcessorStore
import cn.netdiscovery.http.core.serializer.Serializer
import cn.netdiscovery.http.core.serializer.SerializerManager
import cn.netdiscovery.http.core.interceptor.ResponseProcessingInterceptor
import cn.netdiscovery.http.core.request.converter.RequestJSONConverter
import cn.netdiscovery.http.core.storage.DefaultStorage
import cn.netdiscovery.http.core.storage.Storage
import cn.netdiscovery.http.core.storage.cookie.JavaNetCookieJar
import okhttp3.*
import java.io.File
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.Proxy
import java.net.ProxySelector
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.HttpClientBuilder
 * @author: Tony Shen
 * @date: 2020-10-04 01:34
 * @version: V1.0 <描述当前版本功能>
 */
class HttpClientBuilder {

    private val builder = OkHttpClient.Builder()

    private var baseUrl: String = ""
    private var processorStore = DefaultProcessorStore(mutableListOf(), mutableListOf())
    private var storageProvider: Storage? = null
    private var cookiesFileName: String = "default.cookies"
    private var cookieManager: CookieManager? = null
    private var userAgent: String? = null
    private var jsonConverterClass:KClass<out RequestJSONConverter>? = null

    private var isCookieJar = false
    private var isCache = false

    fun connectTimeout(timeout: Long, unit: TimeUnit): HttpClientBuilder {
        builder.connectTimeout(timeout, unit)
        return this
    }

    fun readTimeout(timeout: Long, unit: TimeUnit): HttpClientBuilder {
        builder.readTimeout(timeout, unit)
        return this
    }

    fun writeTimeout(timeout: Long, unit: TimeUnit): HttpClientBuilder {
        builder.writeTimeout(timeout, unit)
        return this
    }

    fun pingInterval(interval: Long, unit: TimeUnit): HttpClientBuilder {
        builder.pingInterval(interval, unit)
        return this
    }

    fun proxy(proxy: Proxy?): HttpClientBuilder {
        builder.proxy(proxy)
        return this
    }

    fun proxySelector(proxySelector: ProxySelector): HttpClientBuilder {
        builder.proxySelector(proxySelector)
        return this
    }

    fun cookieJar(cookieJar: CookieJar): HttpClientBuilder {
        isCookieJar = true
        builder.cookieJar(cookieJar)
        return this
    }

    fun cache(cache: Cache?): HttpClientBuilder {
        isCache = true
        builder.cache(cache)
        return this
    }

    fun dns(dns: Dns): HttpClientBuilder {
        builder.dns(dns)
        return this
    }

    fun socketFactory(socketFactory: SocketFactory): HttpClientBuilder {
        builder.socketFactory(socketFactory)
        return this
    }

    fun sslSocketFactory(sslSocketFactory: SSLSocketFactory, trustManager: X509TrustManager): HttpClientBuilder {
        builder.sslSocketFactory(sslSocketFactory, trustManager)
        return this
    }

    fun hostnameVerifier(hostnameVerifier: HostnameVerifier): HttpClientBuilder {
        builder.hostnameVerifier(hostnameVerifier)
        return this
    }

    fun certificatePinner(certificatePinner: CertificatePinner): HttpClientBuilder {
        builder.certificatePinner(certificatePinner)
        return this
    }

    fun authenticator(authenticator: Authenticator): HttpClientBuilder {
        builder.authenticator(authenticator)
        return this
    }

    fun proxyAuthenticator(proxyAuthenticator: Authenticator): HttpClientBuilder {
        builder.proxyAuthenticator(proxyAuthenticator)
        return this
    }

    fun connectionPool(connectionPool: ConnectionPool): HttpClientBuilder {
        builder.connectionPool(connectionPool)
        return this
    }

    fun followSslRedirects(followProtocolRedirects: Boolean): HttpClientBuilder {
        builder.followSslRedirects(followProtocolRedirects)
        return this
    }

    fun followRedirects(followRedirects: Boolean): HttpClientBuilder {
        builder.followRedirects(followRedirects)
        return this
    }

    fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean): HttpClientBuilder {
        builder.retryOnConnectionFailure(retryOnConnectionFailure)
        return this
    }

    /**
     * 通过构造 okhttp 的 dispatcher，来支持自定义线程池
     */
    fun dispatcher(dispatcher: Dispatcher): HttpClientBuilder {
        builder.dispatcher(dispatcher)
        return this
    }

    fun protocols(protocols: MutableList<Protocol>): HttpClientBuilder {
        builder.protocols(protocols)
        return this
    }

    fun connectionSpecs(connectionSpecs: List<ConnectionSpec>): HttpClientBuilder {
        builder.connectionSpecs(connectionSpecs)
        return this
    }

    fun addInterceptor(interceptor: Interceptor): HttpClientBuilder {
        builder.addInterceptor(interceptor)
        return this
    }

    fun addNetworkInterceptor(interceptor: Interceptor): HttpClientBuilder {
        builder.addNetworkInterceptor(interceptor)
        return this
    }

    fun eventListener(eventListener: EventListener): HttpClientBuilder {
        builder.eventListener(eventListener)
        return this
    }

    fun eventListenerFactory(eventListenerFactory: EventListener.Factory): HttpClientBuilder {
        builder.eventListenerFactory(eventListenerFactory)
        return this
    }

    fun allTimeouts(timeout: Long, unit: TimeUnit): HttpClientBuilder {
        connectTimeout(timeout, unit)
        readTimeout(timeout, unit)
        writeTimeout(timeout, unit)
        return this
    }

    fun baseUrl(url: String): HttpClientBuilder {
        baseUrl = url
        return this
    }

    fun addRequestProcessor(processor: RequestProcessor): HttpClientBuilder {
        processorStore.addRequestProcessor(processor)
        return this
    }

    fun addResponseProcessor(processor: ResponseProcessor): HttpClientBuilder {
        processorStore.addResponseProcessor(processor)
        return this
    }

    fun storageProvider(storageProvider: Storage): HttpClientBuilder {
        this.storageProvider = storageProvider
        return this
    }

    fun cookiesFileName(fileName: String): HttpClientBuilder {
        cookiesFileName = fileName
        return this
    }

    fun cookieManager(cookieManager: CookieManager): HttpClientBuilder {
        this.cookieManager = cookieManager
        return this
    }

    fun userAgent(ua: String): HttpClientBuilder {
        userAgent = ua
        return this
    }

    fun converter(converter:Serializer): HttpClientBuilder {
        SerializerManager.converter(converter)
        return this
    }

    /**
     * 全局的 RequestJSONConverter
     */
    fun jsonConverter(jsonConverterClass:KClass<out RequestJSONConverter>): HttpClientBuilder {
        this.jsonConverterClass = jsonConverterClass
        return this
    }

    /**
     * 构建 OkHttpClient，便于单独使用 OkHttpClient 或者给其他的库使用
     */
    fun buildOkHttpClient():OkHttpClient = builder.build()

    /**
     * 构建 HttpClient
     */
    fun build(): HttpClient {

        addInterceptor(ResponseProcessingInterceptor(processorStore))

        storageProvider = storageProvider ?: DefaultStorage()
        cookieManager = cookieManager ?: CookieManager().apply { this.setCookiePolicy(CookiePolicy.ACCEPT_ALL) }

        // TODO：是否需要？
        if(!isCookieJar) {
            cookieJar(JavaNetCookieJar(cookieManager!!))
        }

        // TODO：是否需要？
        if(!isCache) {
            cache(Cache(File(storageProvider!!.cacheDir, getCacheFileName(baseUrl)), 1024))
        }

        val okHttpClient = builder.build()

        return OkHttpClientWrapper(
                baseUrl = baseUrl,
                okHttpClient = okHttpClient,
                processorStore = processorStore,
                cookieManager = cookieManager,
                storageProvider = storageProvider!!,
                cookiesFileName = cookiesFileName
        ).apply {
            userAgent?.let {
                this.userAgent(it)
            }

            jsonConverterClass?.let {
                this.jsonConverter(it)
            }
        }
    }

    private fun getCacheFileName(url: String) = url.replace("http://", "")
            .replace("https://", "")
            .replace("www.", "")
            .replace("/", "_")
            .let { "$it.cache" }
}
