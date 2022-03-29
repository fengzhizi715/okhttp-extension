package cn.netdiscovery.http.core.dsl.ssl

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.ssl.UnsafeTrustManager
 * @author: Tony Shen
 * @date: 2022/3/30 1:09 上午
 * @version: V1.0 <描述当前版本功能>
 */
class UnsafeTrustManager : X509TrustManager {

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}