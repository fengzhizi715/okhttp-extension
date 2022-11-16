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

    override fun callStart(call: Call) {
        super.callStart(call)
        callStartTime = System.nanoTime()

        val request = call.request()
        md.ua = userAgent
        md.url = request.url.toString()
        md.requestMethod = request.method
        md.isHttps = request.isHttps
        md.netType = ""
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        dnsStartTime = System.nanoTime()
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        dnsEndTime = System.nanoTime()

        md.dnsResult = inetAddressList.toString()
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        connectStartTime = System.nanoTime()

        md.ip = inetSocketAddress.address.hostAddress
        md.port = inetSocketAddress.port.toString()
        md.isProxy = proxy.type() != Proxy.Type.DIRECT
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        secureConnectStartTime = System.nanoTime()
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        secureConnectEndTime = System.nanoTime()

        md.tlsHandshakeInfo = handshake?.toString() ?: ""
    }

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        connectEndTime = System.nanoTime()

        md.protocol = protocol.toString()
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        callEndTime = System.nanoTime()

        md.protocol = protocol.toString()
        onEventError(call, ioe)
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        requestHeadersStartTime = System.nanoTime()
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        requestHeadersEndTime = System.nanoTime()
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        requestBodyStartTime = System.nanoTime()
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        requestBodyEndTime = System.nanoTime()

        md.requestBodyByteCount = byteCount
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        callEndTime = System.nanoTime()

        onEventError(call, ioe)
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        responseHeadersStartTime = System.nanoTime()
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        responseHeadersEndTime = System.nanoTime()

        md.responseCode = response.code
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        responseBodyStartTime = System.nanoTime()
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        responseBodyEndTime = System.nanoTime()

        md.responseBodyByteCount = byteCount
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        callEndTime = System.nanoTime()

        onEventError(call, ioe)
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        callEndTime = System.nanoTime()

        calculateTime()
        netMonitorCallback.onSuccess(call, md)
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        callEndTime = System.nanoTime()

        onEventError(call, ioe)
    }

    /**
     * 计算各个阶段耗时
     */
    private fun calculateTime() {
        md.dnsCost = getCost(dnsEndTime, dnsStartTime)
        md.tcpCost = getCost(connectEndTime, connectStartTime)
        md.tlsCost = getCost(secureConnectEndTime, secureConnectStartTime)
        md.conectTotalCost = md.tcpCost + md.tlsCost
        md.requestHeaderCost = getCost(requestHeadersEndTime, requestHeadersStartTime)
        md.requestBodyCost = getCost(requestBodyEndTime, requestBodyStartTime)
        md.requestTotalCost = md.requestHeaderCost + md.requestBodyCost
        md.responseHeaderCost = getCost(responseHeadersEndTime, responseHeadersStartTime)
        md.responseBodyCost = getCost(responseBodyEndTime, responseBodyStartTime)
        md.responseTotalCost = md.responseHeaderCost + md.responseBodyCost
        md.callCoat = getCost(callEndTime, callStartTime)
    }

    /**
     * 链路中所有异常，都会调用到这个方法中
     */
    private fun onEventError(call: Call, ioe: IOException) {
        calculateTime()
        netMonitorCallback.onError(call, md, ioe)
    }

    private fun getCost(endNano: Long, startNano: Long): Long = TimeUnit.NANOSECONDS.toMillis(endNano - startNano)
}

interface NetMonitorCallback {
    fun onSuccess(call: Call, monitorResult: MonitorData)

    fun onError(call: Call, monitorResult: MonitorData, ioe: Exception)
}