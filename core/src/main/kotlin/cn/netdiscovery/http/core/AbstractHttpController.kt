package cn.netdiscovery.http.core

import cn.netdiscovery.http.core.domain.*

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.AbstractHttpController
 * @author: Tony Shen
 * @date: 2020-10-11 01:13
 * @version: V1.0 <描述当前版本功能>
 */
abstract class AbstractHttpController(protected val client: HttpClient) {

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> get(get: GetMethod<T>.() -> Unit): ProcessResult<T> {
        val method = GetMethod<T>()
        get(method)
        return client.send(T::class, method)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> post(post: PostMethod<T>.() -> Unit): ProcessResult<T> {
        val method = PostMethod<T>()
        post(method)
        return client.send(T::class, method)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> jsonPost(jsonPost: JsonPostMethod<T>.() -> Unit): ProcessResult<T> {
        val method = JsonPostMethod<T>()
        jsonPost(method)
        return client.send(T::class, method)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> put(put: PutMethod<T>.() -> Unit): ProcessResult<T> {
        val method = PutMethod<T>()
        put(method)
        return client.send(T::class, method)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> jsonPut(jsonPut: JsonPutMethod<T>.() -> Unit): ProcessResult<T> {
        val method = JsonPutMethod<T>()
        jsonPut(method)
        return client.send(T::class, method)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> delete(delete: DeleteMethod<T>.() -> Unit): ProcessResult<T> {
        val method = DeleteMethod<T>()
        delete(method)
        return client.send(T::class, method)
    }
}