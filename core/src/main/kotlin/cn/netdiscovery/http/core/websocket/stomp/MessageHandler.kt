package cn.netdiscovery.http.core.websocket.stomp

import cn.netdiscovery.http.core.exception.StompException
import cn.netdiscovery.http.core.serializer.SerializerManager

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.websocket.stomp.MessageHandler
 * @author: Tony Shen
 * @date: 2021/11/29 4:51 下午
 * @version: V1.0 <描述当前版本功能>
 */
abstract class MessageHandler<T>(val clazz: Class<T>) {

    /**
     * Yields the model <T> of the STOMP message from the json representation.
     *
     * @param payload the json
     * @return the model <T>
     */
    fun <T> toModel(payload: String): T {
        try {
            return SerializerManager.fromJson<T>(payload, clazz)!!
        } catch (e: Exception) {
            throw StompException("Error deserializing model of type ${clazz.name}")
        }
    }

    /**
     * It delivers the result of a parsed STOMP message response.
     * @param result the result as JSON.
     */
    abstract fun deliverResult(result: String)

    /**
     * It delivers an [ErrorModel] which wraps an error.
     * @param errorModel the error model
     */
    abstract fun deliverError(errorModel: ErrorModel)

    /**
     * Special method to deliver a NOP.
     */
    abstract fun deliverNothing()
}