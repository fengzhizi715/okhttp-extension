package cn.netdiscovery.http.core.test

//import freemarker.cache.ClassTemplateLoader
import cn.netdiscovery.http.core.test.domain.RequestModel
import cn.netdiscovery.http.core.test.domain.WrapperResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.content.TextContent
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import java.io.File
import java.text.DateFormat

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.core.Main
 * @author: Tony Shen
 * @date: 2020-10-11 02:02
 * @version: V1.0  Mock 的 Web Server
 */
val gson: Gson = GsonBuilder().setPrettyPrinting().create()

fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Routing) {
        var fileDescription = ""
        var fileName = ""

        get("/sayHi/{name}") {
            val name = call.parameters["name"]
            val text = "hi $name!"
            call.respondText(text)
        }
        get("/response-headers") {
            val params = call.request.headers.toMap()
            val content = TextContent(gson.toJson(params), ContentType.Application.Json)
            call.respond(content)
        }
        get("/response-headers-queries") {
            val headers = call.request.headers.toMap()
            val queries = call.request.queryParameters.toMap()
            val total = headers.plus(queries)
            val content = TextContent(gson.toJson(total), ContentType.Application.Json)
            call.respond(content)
        }
        post("/response-body") {
            val requestBody = call.receiveText()
            call.respondText(requestBody)
        }
        post("/response-body-with-model") {
            val requestModel = call.receive(RequestModel::class)
            val response = WrapperResponse(data = requestModel)
            call.respond(response)
        }
        post("/upload") {
            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        var fileBytes = part.streamProvider().readBytes()
                        File("/Users/tony/$fileName").writeBytes(fileBytes)
                    }
                    else -> {

                    }
                }
            }

            call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
        }
        put("/response-body") {
            val requestBody = call.receiveText()
            call.respondText(requestBody)
        }
        delete("/users/{name}") {
            val name = call.parameters["name"]
            call.respond("$name removed successfully")
        }
        patch("/response-body") {
            val requestBody = call.receiveText()
            call.respondText(requestBody)
        }
        head("/response-headers") {
            val params = call.request.headers.toMap()
            val content = TextContent(gson.toJson(params), ContentType.Application.Json)
            call.respond(content)
        }
    }
}

fun main() {

    embeddedServer(Netty, 8080, watchPaths = listOf("MainKt"), module = Application::module).start()
}
