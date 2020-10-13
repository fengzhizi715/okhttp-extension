import cn.netdiscovery.http.core.AbstractHttpController
import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.domain.Params
import cn.netdiscovery.http.core.domain.ProcessResult
import cn.netdiscovery.http.core.domain.params
import cn.netdiscovery.http.core.request.converter.GlobalRequestJSONConverter
import cn.netdiscovery.http.extension.rxjava3.asObservable
import cn.netdiscovery.http.interceptor.LoggingInterceptor
import cn.netdiscovery.http.interceptor.log.LogManager
import cn.netdiscovery.http.interceptor.log.LogProxy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Dispatcher
import okhttp3.Response
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 *
 * @FileName:
 *          .Test
 * @author: Tony Shen
 * @date: 2020-10-11 01:20
 * @version: V1.0 <描述当前版本功能>
 */

val controller by lazy {

    LogManager.logProxy(object : LogProxy {  // 必须要实现 LogProxy ，否则无法打印网络请求的 request 、response
        override fun e(tag: String, msg: String) {
        }

        override fun w(tag: String, msg: String) {
        }

        override fun i(tag: String, msg: String) {
            println("$tag:$msg")
        }

        override fun d(tag: String, msg: String) {
            println("$tag:$msg")
        }
    })

    val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(true) // TODO: 发布到生产环境需要改成false
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
//        .hideVerticalLine()// 隐藏竖线边框
            .build()

    val client = HttpClientBuilder()
            .baseUrl("http://localhost:8080")
            .addInterceptor(loggingInterceptor)
            .converter(GsonConverter())
            .jsonConverter(GlobalRequestJSONConverter::class)
            .dispatcher(Dispatcher(Executors.newCachedThreadPool()))
            .build()

    TestHttpController(client)
}

fun main() {
    testPostWithResponseMapper()
}

fun testGet() {
    controller.testGet("Tony").sync()
}

fun testGetWithHeader() {
    val header = mutableMapOf<String,String>()
    header["key1"] = "value1"
    header["key2"] = "value2"
    header["key3"] = "value3"
    controller.testGetWithHeader(header).sync()
}

fun testPost() {
    val body = params(
        "name1" to "value1",
        "name2" to "value2",
        "name3" to "value3"
    )
    controller.testPost(body).sync()
}

fun testPostWithModel() {
    val requestBody = RequestBody()
    controller.testPostWithModel(requestBody).sync()
}

fun testPostWithResponseMapper() {
    val requestBody = RequestBody()
    controller.testPostWithResponseMapper(requestBody).sync()
}

fun testPostWithResponseMapper2() {
    val requestBody = RequestBody()
    controller.testPostWithResponseMapper(requestBody).asObservable<ResponseData>()
        .subscribe {
            println(it.content)
        }
}

class TestHttpController(client:HttpClient) : AbstractHttpController(client) {

    fun testGet(name: String) = get<Response> {
        url = "/sayHi/$name"
    }

    fun testGetWithHeader(headers:Map<String,String>) = get<Response> {
        url = "/response-headers"
        headersParams = Params.from(headers)
    }

    fun testPost(body:Params) = post<Response> {
        url = "/response-body"
        bodyParams = body
    }

    fun testPostWithModel(model:RequestBody) = jsonPost<Response>{
        url = "/response-body-with-model"
        jsonModel = model
    }

    fun testPostWithResponseMapper(model:RequestBody) = jsonPost<ResponseData>{
        url = "/response-body-with-model"
        jsonModel = model
        responseMapper = ResponseDataMapper::class
    }
}