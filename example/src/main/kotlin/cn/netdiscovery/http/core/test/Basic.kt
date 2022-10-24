package cn.netdiscovery.http.core.test

import cn.netdiscovery.http.core.utils.extension.httpGet

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.Basic
 * @author: Tony Shen
 * @date: 2021/11/19 1:47 下午
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    "https://baidu.com".httpGet().use {
        println(it)
    }
}