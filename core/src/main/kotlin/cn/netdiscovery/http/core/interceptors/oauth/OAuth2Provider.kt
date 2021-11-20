package cn.netdiscovery.http.core.interceptors.oauth

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.interceptors.oauth.OAuth2Provider
 * @author: Tony Shen
 * @date: 2021/11/20 10:44 上午
 * @version: V1.0 <描述当前版本功能>
 */
interface OAuth2Provider {

    /**
     * 将 token 信息放入 header 中
     */
    val headers: Map<String, String>

    /**
     * 刷新token
     * @return String? 返回刷新后的新token
     */
    fun refreshToken(): String?
}