package cn.netdiscovery.http.core.storage.cookie

import okhttp3.Cookie
import java.io.Serializable
import java.net.HttpCookie

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.storage.cookie.CookieTo
 * @author: Tony Shen
 * @date: 2020-10-03 14:13
 * @version: V1.0 <描述当前版本功能>
 */
data class CookieTo(val name: String, val value: String, val domain: String): Serializable {

    fun toHttpCookie(): HttpCookie {
        val cookie = HttpCookie(name, value)
        cookie.domain = domain
        return cookie
    }

    companion object {
        fun of(cookie: HttpCookie): CookieTo {
            return CookieTo(
                    cookie.name,
                    cookie.value,
                    cookie.domain
            )
        }

        fun of(cookie: Cookie): CookieTo {
            return CookieTo(cookie.name, cookie.value, cookie.domain)
        }
    }
}