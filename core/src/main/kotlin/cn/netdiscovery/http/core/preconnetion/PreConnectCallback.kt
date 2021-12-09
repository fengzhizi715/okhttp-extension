package cn.netdiscovery.http.core.preconnetion

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.preconnetion.PreConnectCallback
 * @author: Tony Shen
 * @date: 2021/12/9 11:19 AM
 * @version: V1.0 <描述当前版本功能>
 */
interface PreConnectCallback {

    /**
     * 连接建立完成
     * @param url
     */
    fun connectCompleted(url: String)

    /**
     * 连接失败
     */
    fun connectFailed(t: Throwable)
}