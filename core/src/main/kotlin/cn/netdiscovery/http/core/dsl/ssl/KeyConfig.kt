package cn.netdiscovery.http.core.dsl.ssl

import java.io.InputStream

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.dsl.ssl.KeyConfig
 * @author: Tony Shen
 * @date: 2022/3/30 1:07 上午
 * @version: V1.0 <描述当前版本功能>
 */
@SSLDslMarker
class KeyConfig(val inputStream: InputStream) {
    var algorithm: String? = null
    var password: CharArray? = null
    var fileType: String = "JKS"

    @Suppress("unused")
    infix fun withPass(pass: String) = apply {
        password = pass.toCharArray()
    }

    @Suppress("unused")
    infix fun ofType(type: String) = apply {
        fileType = type
    }

    @Suppress("unused")
    infix fun using(algorithm: String) = apply {
        this.algorithm = algorithm
    }

}