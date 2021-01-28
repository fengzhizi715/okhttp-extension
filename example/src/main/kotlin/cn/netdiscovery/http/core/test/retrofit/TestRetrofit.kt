package cn.netdiscovery.http.core.test.retrofit

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.test.retrofit.TestRetrofit
 * @author: Tony Shen
 * @date: 2021-01-27 10:53
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    responseHeaders().subscribe {
        println(Thread.currentThread().name)
        println(it)
    }
}