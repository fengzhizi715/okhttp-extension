package cn.netdiscovery.http.core.monitor

import okhttp3.*
import okhttp3.internal.userAgent
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * 通过域名访问的方式来请求网络时，会经历下列过程：
 * 1. DNS解析：通过域名服务器或者本地host将域名解析成ip地址
 * 2. 建立连接：三次握手
 * 3. 发送数据：通过 GET/POST/PUT 等方式将数据(header和body)发送给服务器，
 * 4. 接受数据：接受服务器返回数据：响应头和body
 * 5. 断开链接：四次挥手断开链接
 * @FileName:
 *          cn.netdiscovery.http.core.monitor.HttpClientMonitor
 * @author: Tony Shen
 * @date: 2022/10/26 11:59 下午
 * @version: V1.0 <描述当前版本功能>
 */
class HttpClientMonitor(
    private val netMonitorCallback: NetMonitorCallback,
): EventListener()  {

    private val md = MonitorData()

    var callStartTime = 0L
    var dnsStartTime = 0L
    var dnsEndTime = 0L
    var connectStartTime = 0L
    var secureConnectStartTime = 0L
    var secureConnectEndTime = 0L
    var connectEndTime = 0L
    var requestHeadersStartTime = 0L
    var requestHeadersEndTime = 0L
    var requestBodyStartTime = 0L
    var requestBodyEndTime = 0L
    var responseHeadersStartTime = 0L
    var responseHeadersEndTime = 0L
    var responseBodyStartTime = 0L
    var responseBodyEndTime = 0L
    var callEndTime = 0L

    /**
     * Invoked as soon as a call is enqueued or executed by a client. In case of thread or stream
     * limits, this call may be executed well before processing the request is able to begin.
     *
     *
     * This will be invoked only once for a single [Call]. Retries of different routes
     * or redirects will be handled within the boundaries of a single callStart and [ ][.callEnd]/[.callFailed] pair.
     */
    override fun callStart(call: Call) {
        super.callStart(call)
        callStartTime = System.currentTimeMillis()

        val request = call.request()
        md.ua = userAgent
        md.url = request.url.toString()
        md.requestMethod = request.method
        md.isHttps = request.isHttps
        md.netType = ""
    }

    /**
     * Invoked just prior to a DNS lookup. See [Dns.lookup].
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different host.
     *
     *
     * If the [Call] is able to reuse an existing pooled connection, this method will not be
     * invoked. See [ConnectionPool].
     */
    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        dnsStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after a DNS lookup.
     *
     *
     * This method is invoked after [.dnsStart].
     */
    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        dnsEndTime = System.currentTimeMillis()

        md.dnsResult=inetAddressList.toString()
    }

    /**
     * Invoked just prior to initiating a socket connection.
     *
     *
     * This method will be invoked if no existing connection in the [ConnectionPool] can be
     * reused.
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address, or a connection is retried.
     */
    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        connectStartTime = System.currentTimeMillis()

        md.ip = inetSocketAddress.address.hostAddress
        md.port = inetSocketAddress.port.toString()
        md.isProxy = proxy.type() != Proxy.Type.DIRECT
    }

    /**
     * Invoked just prior to initiating a TLS connection.
     *
     *
     * This method is invoked if the following conditions are met:
     *
     *  * The [Call.request] requires TLS.
     *  * No existing connection from the [ConnectionPool] can be reused.
     *
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address, or a connection is retried.
     */
    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        secureConnectStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after a TLS connection was attempted.
     *
     *
     * This method is invoked after [.secureConnectStart].
     */
    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        secureConnectEndTime = System.currentTimeMillis()

        md.tlsHandshakeInfo = handshake?.toString() ?: ""
    }

    /**
     * Invoked immediately after a socket connection was attempted.
     *
     *
     * If the `call` uses HTTPS, this will be invoked after
     * [.secureConnectEnd], otherwise it will invoked after
     * [.connectStart].
     */
    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        connectEndTime = System.currentTimeMillis()

        md.protocol = protocol.toString()
    }

    /**
     * Invoked when a connection attempt fails. This failure is not terminal if further routes are
     * available and failure recovery is enabled.
     *
     *
     * If the `call` uses HTTPS, this will be invoked after [.secureConnectEnd], otherwise it will invoked after [.connectStart].
     */
    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        callEndTime = System.currentTimeMillis()

        md.protocol = protocol.toString()
        onEventError(call, ioe)
    }


    /**
     * Invoked after a connection has been acquired for the `call`.
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address.
     */
    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
    }

    /**
     * Invoked after a connection has been released for the `call`.
     *
     *
     * This method is always invoked after [.connectionAcquired].
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address.
     */
    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
    }

    /**
     * Invoked just prior to sending request headers.
     *
     *
     * The connection is implicit, and will generally relate to the last
     * [.connectionAcquired] event.
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address.
     */
    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        requestHeadersStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after sending request headers.
     *
     *
     * This method is always invoked after [.requestHeadersStart].
     *
     * @param request the request sent over the network. It is an error to access the body of this
     * request.
     */
    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        requestHeadersEndTime = System.currentTimeMillis()
    }

    /**
     * Invoked just prior to sending a request body.  Will only be invoked for request allowing and
     * having a request body to send.
     *
     *
     * The connection is implicit, and will generally relate to the last
     * [.connectionAcquired] event.
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address.
     */
    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        requestBodyStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after sending a request body.
     *
     *
     * This method is always invoked after [.requestBodyStart].
     */
    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        requestBodyEndTime = System.currentTimeMillis()

        md.requestBodyByteCount = byteCount
    }

    /**
     * Invoked when a request fails to be written.
     *
     *
     * This method is invoked after [.requestHeadersStart] or [.requestBodyStart]. Note
     * that request failures do not necessarily fail the entire call.
     */
    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        callEndTime = System.currentTimeMillis()

        onEventError(call, ioe)
    }

    /**
     * Invoked just prior to receiving response headers.
     *
     *
     * The connection is implicit, and will generally relate to the last
     * [.connectionAcquired] event.
     *
     *
     * This can be invoked more than 1 time for a single [Call]. For example, if the response
     * to the [Call.request] is a redirect to a different address.
     */
    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        responseHeadersStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after receiving response headers.
     *
     *
     * This method is always invoked after [.responseHeadersStart].
     *
     * @param response the response received over the network. It is an error to access the body of
     * this response.
     */
    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        responseHeadersEndTime = System.currentTimeMillis()

        md.responseCode = response.code
    }

    /**
     * Invoked just prior to receiving the response body.
     *
     *
     * The connection is implicit, and will generally relate to the last
     * [.connectionAcquired] event.
     *
     *
     * This will usually be invoked only 1 time for a single [Call],
     * exceptions are a limited set of cases including failure recovery.
     */
    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        responseBodyStartTime = System.currentTimeMillis()
    }

    /**
     * Invoked immediately after receiving a response body and completing reading it.
     *
     *
     * Will only be invoked for requests having a response body e.g. won't be invoked for a
     * websocket upgrade.
     *
     *
     * This method is always invoked after [.requestBodyStart].
     */
    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        responseBodyEndTime = System.currentTimeMillis()

        md.responseBodyByteCount = byteCount
    }

    /**
     * Invoked when a response fails to be read.
     *
     *
     * This method is invoked after [.responseHeadersStart] or [.responseBodyStart].
     * Note that response failures do not necessarily fail the entire call.
     */
    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        callEndTime = System.currentTimeMillis()

        onEventError(call, ioe)
    }

    /**
     * Invoked immediately after a call has completely ended.  This includes delayed consumption
     * of response body by the caller.
     *
     *
     * This method is always invoked after [.callStart].
     */
    override fun callEnd(call: Call) {
        super.callEnd(call)
        callEndTime = System.currentTimeMillis()

        calculateTime()
        netMonitorCallback.onSuccess(call, md)
    }

    /**
     * Invoked when a call fails permanently.
     *
     *
     * This method is always invoked after [.callStart].
     */
    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        callEndTime = System.currentTimeMillis()

        onEventError(call, ioe)
    }


    /**
     * 计算各个阶段耗时
     */
    private fun calculateTime() {
        md.dnsCost = (dnsEndTime - dnsStartTime).toInt()
        md.tcpCost = (secureConnectStartTime - connectStartTime).toInt()
        md.tlsCost = (secureConnectEndTime - secureConnectEndTime).toInt()
        md.conectTotalCost = md.tcpCost + md.tlsCost
        md.requestHeaderCost = (requestHeadersEndTime - requestHeadersStartTime).toInt()
        md.requestBodyCost = (requestBodyEndTime - requestBodyStartTime).toInt()
        md.requestTotalCost = md.requestHeaderCost + md.requestBodyCost
        md.responseHeaderCost = (responseHeadersEndTime - responseHeadersStartTime).toInt()
        md.responseBodyCost = (responseBodyEndTime - responseBodyStartTime).toInt()
        md.responseTotalCost = md.responseHeaderCost + md.responseBodyCost
        md.callCoat = (callEndTime - callStartTime).toInt()
    }


    /**
     * 链路中 所有异常 都会调用到这个方法中
     */
    private fun onEventError(call: Call, ioe: IOException) {
        calculateTime()
        netMonitorCallback.onError(call, md, ioe)
    }

    private fun getCost(startNano: Long): Long = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano)
}

interface NetMonitorCallback {
    fun onSuccess(call: Call, monitorResult: MonitorData)

    fun onError(call: Call, monitorResult: MonitorData, ioe: Exception)
}