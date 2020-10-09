package cn.netdiscovery.http.core.processor.method

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.domain.response.ResponseConsumer
import cn.netdiscovery.http.core.domain.Content
import cn.netdiscovery.http.core.domain.JsonContent
import cn.netdiscovery.http.core.exception.IterableModelNotFoundException
import cn.netdiscovery.http.core.extension.collect
import cn.netdiscovery.http.core.domain.RequestMethod
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.processor.method.IterableMethodProcessor
 * @author: Tony Shen
 * @date: 2020-10-09 00:50
 * @version: V1.0 <描述当前版本功能>
 */
class IterableMethodProcessor<T : Any>(
        private val method: RequestMethod<*>,
        private val client: HttpClient,
        private val iterableContent: Content,
        private val contents: List<Content>,
        private val jsonContent: JsonContent?
) : AbstractRequestMethodProcessor<T>() {

    @Throws(IterableModelNotFoundException::class, ClassCastException::class)
    override fun process(): ResponseConsumer<T> {
        return toResponses().collect()
    }

    override fun processAsync(): CompletableFuture<ResponseConsumer<T>> {
        return asyncToResponses().collect().thenApply { it.collect() }
    }

    private fun createStream(iterableModel: Iterable<*>): Stream<*> {
        return if (method.parallel) {
            iterableModel.toList().parallelStream()
        } else {
            iterableModel.toList().stream()
        }
    }

    private fun toResponses(): List<ResponseConsumer<T>> {
        val model = iterableContent.model ?: throw IterableModelNotFoundException()

        model as Iterable<*>

        val modelStream = createStream(model)

        return modelStream.map { m ->

            val contentMap = contents.map { it.name to it }.toMap().toMutableMap()

            val modelContent = Content(
                    name = iterableContent.name,
                    params = iterableContent.params,
                    map = iterableContent.map,
                    model = m,
                    modelConverter = iterableContent.modelConverter
            )

            contentMap[modelContent.name] = modelContent

            val singleMethodProcessor = SingleMethodProcessor<T>(method, client, contentMap.values.toList(), jsonContent)

            singleMethodProcessor.process()
        }.collect(Collectors.toList()).filterNotNull()
    }

    private fun asyncToResponses(): List<CompletableFuture<ResponseConsumer<T>>> {
        val model = iterableContent.model ?: throw IterableModelNotFoundException()

        model as Iterable<*>

        val modelStream = createStream(model)

        return modelStream.map { m ->
            val contentMap = contents.map { it.name to it }.toMap().toMutableMap()

            val modelContent = Content(
                    name = iterableContent.name,
                    params = iterableContent.params,
                    map = iterableContent.map,
                    model = m,
                    modelConverter = iterableContent.modelConverter
            )

            contentMap[modelContent.name] = modelContent

            val singleMethodProcessor = SingleMethodProcessor<T>(method, client, contentMap.values.toList(), jsonContent)

            singleMethodProcessor.processAsync()
        }.collect(Collectors.toList()).filterNotNull()
    }
}