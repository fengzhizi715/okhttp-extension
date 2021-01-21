package cn.netdiscovery.http.core

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.Cancelable
 * @author: Tony Shen
 * @date: 2020-10-10 12:39
 * @version: V1.0 取消网络请求的接口
 */
interface Cancelable {

    fun cancel()
}