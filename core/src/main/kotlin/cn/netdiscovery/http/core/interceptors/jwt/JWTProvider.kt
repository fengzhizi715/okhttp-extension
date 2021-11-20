package cn.netdiscovery.http.core.interceptors.jwt

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.jwt.JWTProvider
 * @author: Tony Shen
 * @date: 2021/11/20 12:30 下午
 * @version: V1.0 <描述当前版本功能>
 */
interface JWTProvider {

    fun getToken():String

    /**
     * 刷新token
     * @return String?
     */
    fun refreshToken(): String?
}