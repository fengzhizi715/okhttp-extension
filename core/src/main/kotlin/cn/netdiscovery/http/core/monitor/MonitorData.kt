package cn.netdiscovery.http.core.monitor

import okhttp3.Call
import java.net.InetSocketAddress




/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.monitor.MonitorData
 * @author: Tony Shen
 * @date: 2022/10/27 12:08 上午
 * @version: V1.0 <描述当前版本功能>
 */
data class MonitorData(
    var netType: String = "",//网络状态 NO，2G，3G，4G，5G，WIFI
    var ua: String = "",//okhttp 版本号
    var url: String = "",
    var requestMethod: String = "",
    var isHttps: Boolean = false,
    var ip: String = "", // 目标机器 ip
    var port: String = "", // 端口号
    var isProxy: Boolean = false,//是否 有代理
    var protocol: String = "", // 协议 具体查看 okhttp3.Protocol 类
    var dnsResult: String = "", // dns 解析出来的 ip 信息，用于分析是否存在域名劫持的问题。
    var tlsHandshakeInfo: String = "", // tls 握手记录
    var requestBodyByteCount: Long = 0,//请求体 大小 byte
    var responseBodyByteCount: Long = 0,//响应体 大小 byte
    var responseCode: Int = 0,
    var dnsCost: Int = 0, //ms
    var tcpCost: Int = 0,//ms
    var tlsCost: Int = 0,//ms
    var conectTotalCost: Int = 0, //ms   等于 tcpCost + tlsCost
    var requestHeaderCost: Int = 0, //ms
    var requestBodyCost: Int = 0,//ms
    var requestTotalCost: Int = 0,//ms   等于requestHeaderCost + requestBodyCost
    var responseHeaderCost: Int = 0,//ms
    var responseBodyCost: Int = 0,//ms
    var responseTotalCost: Int = 0, //ms    等于 responseHeaderCost + responseBodyCost
    var callCoat: Int = 0 //  ms 整个链路总耗时
)