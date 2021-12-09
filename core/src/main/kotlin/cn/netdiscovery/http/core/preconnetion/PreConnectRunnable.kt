package cn.netdiscovery.http.core.preconnetion

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.internal.connection.RealConnection
import okhttp3.internal.connection.RealConnectionPool
import okhttp3.internal.connection.RouteSelector
import okio.Timeout
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.preconnetion.PreConnectRunnable
 * @author: Tony Shen
 * @date: 2021/12/9 11:37 AM
 * @version: V1.0 <描述当前版本功能>
 */
internal class PreConnectRunnable(private val okHttpClient: OkHttpClient,private val url:String,private val callback: PreConnectCallback): Runnable  {

    val NONE_CALL: Call = object : Call {
        override fun request(): Request {
            return null!!
        }

        @Throws(IOException::class)
        override fun execute(): Response {
            return null!!
        }

        override fun enqueue(callback: Callback) {}

        override fun cancel() {}

        override fun isExecuted(): Boolean {
            return false
        }

        override fun isCanceled(): Boolean {
            return false
        }

        override fun timeout(): Timeout {
            return null!!
        }

        override fun clone(): Call {
            return null!!
        }
    }

    override fun run() {
        try {
            val httpUrl = createHttpUrl(url)
            if (httpUrl == null) {
                callConnectFailed(callback, IllegalArgumentException("unexpected url: $url"))
                return
            }

            val address = createAddress(okHttpClient, httpUrl)
            val routes = selectRoutes(okHttpClient, address)
            if (routes == null || routes.isEmpty()) {
                callConnectFailed(callback, IOException("No route available."))
                return
            }

            val realConnectionPool = findRealConnectionPool(okHttpClient)
            if (hasPooledConnection(realConnectionPool, address, routes, false)) {
                callConnectFailed(callback, IllegalStateException("There is already a connection with the same address.[1]"))
                return
            }

            val route = routes[0]
            val connection = RealConnection(realConnectionPool, route)
            // 开始连接，如果失败，内部将抛异常
            connection.connect(
                okHttpClient.connectTimeoutMillis,
                okHttpClient.readTimeoutMillis,
                okHttpClient.writeTimeoutMillis,
                okHttpClient.pingIntervalMillis,
                false,
                NONE_CALL,
                EventListener.NONE
            )
            okHttpClient.routeDatabase.connected(connection.route())

            if (hasPooledConnection(realConnectionPool, address, routes, true)) {
                try {
                    connection.socket().close()
                } catch (t: Throwable) {
                }
                callConnectFailed(callback, java.lang.IllegalStateException("There is already a connection with the same address.[2]"))
                return
            }

            synchronized(connection) { realConnectionPool.put(connection) }

            callConnectCompleted(callback, url)
        } catch (t: Throwable) {
            t.printStackTrace();
            callConnectFailed(callback, t)
        }
    }

    private fun createHttpUrl(oriUrl: String): HttpUrl? {
        var url = oriUrl
        if (url.regionMatches(0, "ws:", 0, 3, ignoreCase = true)) {
            url = "http:" + url.substring(3)
        } else if (url.regionMatches(0, "wss:", 0, 4, ignoreCase = true)) {
            url = "https:" + url.substring(4)
        }
        return url.toHttpUrlOrNull()
    }

    private fun createAddress(client: OkHttpClient, url: HttpUrl): Address {
        var sslSocketFactory: SSLSocketFactory? = null
        var hostnameVerifier: HostnameVerifier? = null
        var certificatePinner: CertificatePinner? = null

        if (url.isHttps) {
            sslSocketFactory = client.sslSocketFactory
            hostnameVerifier = client.hostnameVerifier
            certificatePinner = client.certificatePinner
        }
        return Address(
            url.host,
            url.port,
            client.dns,
            client.socketFactory,
            sslSocketFactory,
            hostnameVerifier,
            certificatePinner,
            client.proxyAuthenticator,
            client.proxy,
            client.protocols,
            client.connectionSpecs,
            client.proxySelector
        )
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun findRealConnectionPool(client: OkHttpClient): RealConnectionPool {
        val delegateField = client.connectionPool.javaClass.getDeclaredField("delegate")
        delegateField.isAccessible = true
        return delegateField[client.connectionPool] as RealConnectionPool
    }

    /**
     * 检查是否已经有缓存的相同地址的连接
     */
    private fun hasPooledConnection(realConnectionPool: RealConnectionPool, address: Address, routes: List<Route>, requireMultiplexed: Boolean): Boolean {
        return try {
            val connectionsField = realConnectionPool.javaClass.getDeclaredField("connections")
            connectionsField.isAccessible = true
            val connections = connectionsField[realConnectionPool] as ConcurrentLinkedQueue<RealConnection>

            // 遍历现存connection
            for (connection in connections) {
                synchronized(connection) {
                    var dup = true

                    if (requireMultiplexed) {
                        val isMultiplexed = connection.javaClass.getDeclaredMethod("isMultiplexed\$okhttp")
                        isMultiplexed.isAccessible = true
                        val value = isMultiplexed.invoke(connection) as Boolean
                        if (!value) {
                            dup = false
                        }
                    }

                    if (dup) {
                        val isEligible = connection.javaClass.getDeclaredMethod("isEligible\$okhttp", Address::class.java, MutableList::class.java)
                        isEligible.isAccessible = true
                        val result = isEligible.invoke(connection, address, routes) as Boolean
                        if (!result) {
                            dup = false
                        }
                    }

                    if (dup) {
                        // 已经存在connection
                        return true
                    }
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnsupportedOperationException("Does not support the current version of okhttp.")
        }
    }

    @Throws(IOException::class)
    private fun selectRoutes(client: OkHttpClient, address: Address): List<Route>? {
        val routeSelector = RouteSelector(address, client.routeDatabase, NONE_CALL, EventListener.NONE)
        val selection = if (routeSelector.hasNext()) routeSelector.next() else null
        return selection?.routes
    }

    private fun callConnectCompleted(callback: PreConnectCallback, url: String) {
        callback.connectCompleted(url)
    }

    private fun callConnectFailed(callback: PreConnectCallback, t: Throwable) {
        callback.connectFailed(t)
    }
}