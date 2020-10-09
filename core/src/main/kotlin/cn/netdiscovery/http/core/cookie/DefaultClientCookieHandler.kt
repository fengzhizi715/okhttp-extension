package cn.netdiscovery.http.core.cookie

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.storage.Storage
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookieStore
import java.net.HttpCookie

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.cookie.DefaultClientCookieHandler
 * @author: Tony Shen
 * @date: 2020-10-03 14:02
 * @version: V1.0 <描述当前版本功能>
 */
class DefaultClientCookieHandler(private val client: HttpClient,
                                 private val cookieManager: CookieManager?,
                                 private val storageProvider: Storage,
                                 private val cookiesFileName: String? = null) : ClientCookieHandler {

    override fun addCookie(cookie: HttpCookie) {
        addCookie(cookie.name, cookie.value)
    }

    override fun addCookie(cookies: List<HttpCookie>) {
        val clientCookies = cookies.map {
            return@map Cookie.Builder()
                    .name(it.name)
                    .value(it.value)
                    .domain(getSafeDomain(it.domain))
                    .expiresAt(it.maxAge)
                    .path(it.path)
                    .build()
        }

        addCookieToClient(clientCookies.toMutableList())
    }

    override fun addCookie(name: String, value: String) {
        val cookie = Cookie.Builder()
                .name(name)
                .value(value)
                .domain(getSafeDomain(null))
                .build()

        addCookie(cookie)
    }

    override fun addCookie(cookie: Cookie) {
        addCookieToClient(mutableListOf(cookie))
    }

    override fun addCookieToClient(cookies: MutableList<Cookie>) {
        client.okHttpClient()
                .cookieJar
                .saveFromResponse(client.getBaseUrl().toHttpUrlOrNull()!!, cookies)
    }

    override fun getCookies(): List<HttpCookie> = cookieManager?.cookieStore?.cookies ?: listOf()

    override fun getCookieStore(): CookieStore? = cookieManager?.cookieStore

    override fun saveCookie() {
        if (cookiesFileName != null) {
            val saveFile = File(storageProvider.cookieDir, "$cookiesFileName.cookie")
            val cookies: List<HttpCookie> = getCookies()
            CookieFileStore.saveCookie(cookies, saveFile)
        }
    }

    override fun restoreCookies(fileName: String): List<HttpCookie> {
        var cookies = listOf<HttpCookie>()

        try {
            val saveFile = File(storageProvider.cookieDir, "$fileName.cookie")
            cookies = CookieFileStore.restoreCookie(saveFile) ?: listOf()
        } catch (e: Exception) {
            when (e) {
                is FileNotFoundException -> e.printStackTrace()
                is IOException           -> e.printStackTrace()
                else                     -> throw e
            }
        }

        return cookies
    }

    override fun getCookieManager(): CookieHandler? = cookieManager

    private fun getSafeDomain(domain: String?): String {
        return if (domain == null) {
            var myDomain = client.getBaseUrl()
                    .replace("http://", "")
                    .replace("https://", "")

            if (myDomain.contains("/")) {
                myDomain = myDomain.substring(0, myDomain.indexOf("/"))
            }
            myDomain
        } else {
            domain
        }
    }
}