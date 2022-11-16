package cn.netdiscovery.http.core.monitor


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
    var dnsCost: Long = 0, //ms
    var tcpCost: Long = 0,//ms
    var tlsCost: Long = 0,//ms
    var conectTotalCost: Long = 0, //ms   等于 tcpCost + tlsCost
    var requestHeaderCost: Long = 0, //ms
    var requestBodyCost: Long = 0,//ms
    var requestTotalCost: Long = 0,//ms   等于requestHeaderCost + requestBodyCost
    var responseHeaderCost: Long = 0,//ms
    var responseBodyCost: Long = 0,//ms
    var responseTotalCost: Long = 0, //ms    等于 responseHeaderCost + responseBodyCost
    var callCoat: Long = 0 //  ms 整个链路总耗时
)