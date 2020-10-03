package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.cookie.ClientCookieHandler
import cn.netdiscovery.http.core.cookie.DefaultClientCookieHandler
import cn.netdiscovery.http.core.processor.ProcessorStore
import cn.netdiscovery.http.core.storage.DefaultStorage
import cn.netdiscovery.http.core.storage.Storage
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.CookieManager
import java.net.URI

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.OkHttpClientWrapper
 * @author: Tony Shen
 * @date: 2020-09-20 01:02
 * @version: V1.0 <描述当前版本功能>
 */
class OkHttpClientWrapper(private var baseUrl: String,
                          private val okHttpClient: OkHttpClient,
                          private val processorStore: ProcessorStore,
                          private val cookieManager: CookieManager?,
                          private val storageProvider: Storage = DefaultStorage(),
                          cookiesFileName: String?) : HttpClient {

    private var cookieHandler: ClientCookieHandler? = null
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    private var userAgent = ""

    init {

        if (cookieManager != null) {
            if (cookieHandler == null)
                cookieHandler = DefaultClientCookieHandler(this, cookieManager, storageProvider, cookiesFileName)


            if (cookiesFileName != null) {
                cookieHandler!!.restoreCookies(cookiesFileName).forEach {
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

    override fun getClientCookieHandler(): ClientCookieHandler? {
        return cookieHandler
    }

    override fun get(url: String, customUrl: String?, query: Params?, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun post(url: String, customUrl: String?, body: Params, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun put(url: String, customUrl: String?, body: Params, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun delete(url: String, customUrl: String?, query: Params, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun jsonPost(url: String, customUrl: String?, json: String, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun jsonPut(url: String, customUrl: String?, json: String, headers: Params?): Call {
        TODO("Not yet implemented")
    }

    override fun send(request: Request): Call {
        TODO("Not yet implemented")
    }

    override fun processAndSend(request: Request.Builder): Call {
        TODO("Not yet implemented")
    }

    override fun okHttpClient(): OkHttpClient {
        TODO("Not yet implemented")
    }

    override fun getUserAgent(): String? {
        TODO("Not yet implemented")
    }

    override fun getStorageProvider(): Storage {
        TODO("Not yet implemented")
    }

}