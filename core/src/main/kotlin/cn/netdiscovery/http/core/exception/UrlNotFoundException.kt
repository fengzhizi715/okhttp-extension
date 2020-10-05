package cn.netdiscovery.http.core.exception

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.exception.UrlNotFoundException
 * @author: Tony Shen
 * @date: 2020-10-05 11:05
 * @version: V1.0 <描述当前版本功能>
 */
class UrlNotFoundException: NotFoundException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
            message, cause, enableSuppression, writableStackTrace)
}