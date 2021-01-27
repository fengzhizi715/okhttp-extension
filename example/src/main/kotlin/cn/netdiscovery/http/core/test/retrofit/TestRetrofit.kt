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

    val header = mutableMapOf<String, String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"

    apiService.responseHeaders(header)
        .subscribe {
            println(it)
        }
}