package cn.netdiscovery.http.core.storage.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.internal.delimiterOffset
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.Companion.WARN
import okhttp3.internal.trimSubstring
import java.io.IOException
import java.net.CookieHandler
import java.net.HttpCookie
import java.util.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.storage.cookie.JavaNetCookieJar
 * @author: Tony Shen
 * @date: 2020-10-04 01:42
 * @version: V1.0 <描述当前版本功能>
 */
class JavaNetCookieJar(private var cookieHandler: CookieHandler) : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieStrings = cookies.map { toString(it, true) }
        val multimap = Collections.singletonMap<String, List<String>>("Set-Cookie", cookieStrings)
        try {
            cookieHandler.put(url.toUri(), multimap)
        } catch (e: IOException) {
            Platform.get().log("Saving cookies failed for " + url.resolve("/...")!!,WARN, e)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // The RI passes all headers. We don't have 'em, so we don't pass 'em!
        val headers = emptyMap<String, List<String>>()
        val cookieHeaders: Map<String, List<String>>

        try {
            cookieHeaders = cookieHandler.get(url.toUri(), headers)
        } catch (e: IOException) {
            Platform.get().log("Loading cookies failed for " + url.resolve("/...")!!,WARN, e)
            return emptyList()
        }

        val cookies = convertToCookies(url, cookieHeaders)

        return if (cookies != null)
            Collections.unmodifiableList(cookies)
        else
            emptyList()
    }

    protected fun getCookiesHeaders(cookieHeaders: Map<String, List<String>>): MutableList<String> {
        return cookieHeaders.filter {
            ("Cookie".equals(it.key, ignoreCase = true) || "Cookie2".equals(it.key,
                    ignoreCase = true)) && !it.value.isEmpty()
        }.values
                .flatMap { it }
                .toMutableList()
    }

    protected fun convertToCookies(url: HttpUrl, cookieHeaders: Map<String, List<String>>): MutableList<Cookie>? {
        return getCookiesHeaders(cookieHeaders)
                .flatMap { decodeHeaderAsJavaNetCookies(url, it) }
                .toMutableList()
    }

    /**
     * Convert a request header to OkHttp's cookies via [HttpCookie]. That extra step handles
     * multiple cookies in a single request header, which [Cookie.parse] doesn't support.
     */
    protected fun decodeHeaderAsJavaNetCookies(url: HttpUrl, header: String): List<Cookie> {
        val result = ArrayList<Cookie>()
        var pos = 0
        val limit = header.length
        var pairEnd: Int
        while (pos < limit) {
            pairEnd = header.delimiterOffset(";," , pos, limit)
            val equalsSign = header.delimiterOffset('=', pos, pairEnd)
            val name = header.trimSubstring(pos, equalsSign)
            if (name.startsWith("$")) {
                pos = pairEnd + 1
                continue
            }

            // We have either name=value or just a name.
            var value = if (equalsSign < pairEnd)
                header.trimSubstring(equalsSign + 1, pairEnd)
            else
                ""

            // If the value is "quoted", drop the quotes.
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length - 1)
            }

            result.add(Cookie.Builder()
                    .name(name)
                    .value(value)
                    .domain(url.host)
                    .build())
            pos = pairEnd + 1
        }
        return result
    }

    private fun toString(cookie: Cookie, forObsoleteRfc2965: Boolean): String {
        val result = StringBuilder()
        result.append(cookie.name)
        result.append('=')
        result.append(cookie.value)

        if (cookie.persistent) {
            if (cookie.expiresAt == java.lang.Long.MIN_VALUE) {
                result.append("; max-age=0")
            } else {
                // TODO: expires
//                result.append("; expires=").append(HttpDate.format(Date(cookie.expiresAt)))
            }
        }

        if (!cookie.hostOnly) {
            result.append("; domain=")
            if (forObsoleteRfc2965) {
                result.append(".")
            }
            result.append(cookie.domain)
        }

        result.append("; path=").append(cookie.path)

        if (cookie.secure) {
            result.append("; secure")
        }

        if (cookie.httpOnly) {
            result.append("; httponly")
        }

        return result.toString()
    }
}