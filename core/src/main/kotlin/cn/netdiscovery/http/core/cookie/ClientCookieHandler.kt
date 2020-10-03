package cn.netdiscovery.http.core.cookie

import okhttp3.Cookie
import java.net.CookieHandler
import java.net.CookieStore
import java.net.HttpCookie

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.cookie.ClientCookieHandler
 * @author: Tony Shen
 * @date: 2020-09-21 20:12
 * @version: V1.0 <描述当前版本功能>
 */
interface ClientCookieHandler {

    fun addCookie(cookie: HttpCookie)

    fun addCookie(cookies: List<HttpCookie>)

    fun addCookie(name: String, value: String)

    fun addCookie(cookie: Cookie)

    fun addCookieToClient(cookies: MutableList<Cookie>)

    fun getCookies(): List<HttpCookie>

    fun getCookieStore(): CookieStore?

    fun getCookieManager(): CookieHandler?

    fun saveCookie()

    fun restoreCookies(fileName: String): List<HttpCookie>
}