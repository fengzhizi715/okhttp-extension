package cn.netdiscovery.http.core.websocket.stomp

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.Subscription
 * @author: Tony Shen
 * @date: 2021/11/29 5:01 下午
 * @version: V1.0 <描述当前版本功能>
 */
class StompSubscription(
    val topic: String,
    val subscriptionId: String,
    val messageHandler: MessageHandler<*>,
) {
    private val LOCK = Object()
    private var isSubscribed = false

    /**
     * Emits that the subscription is completed.
     */
    fun emitSubscription() {
        synchronized(LOCK) { LOCK.notify() }
    }

    /**
     * Awaits if necessary for the subscription to complete.
     */
    fun awaitSubscription() {
        synchronized(LOCK) {
            if (!isSubscribed) try {
                LOCK.wait()
                isSubscribed = true
            } catch (e: InterruptedException) {
            }
        }
    }
}