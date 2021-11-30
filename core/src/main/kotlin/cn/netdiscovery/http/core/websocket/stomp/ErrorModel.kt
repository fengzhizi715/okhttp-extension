package cn.netdiscovery.http.core.websocket.stomp

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.ErrorModel
 * @author: Tony Shen
 * @date: 2021/11/29 4:59 下午
 * @version: V1.0 <描述当前版本功能>
 */
data class ErrorModel(val message: String, val exceptionClassName: String) {

    override fun toString(): String = "${this.exceptionClassName} {$this.message}"
}
