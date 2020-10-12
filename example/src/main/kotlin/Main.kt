import com.google.gson.Gson
import com.google.gson.GsonBuilder
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import java.text.DateFormat

/**
 *
 * @FileName:
 *          .Main
 * @author: Tony Shen
 * @date: 2020-10-11 02:02
 * @version: V1.0 <描述当前版本功能>
 */
val gson: Gson = GsonBuilder().setPrettyPrinting().create()

fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        defaultEncoding = "utf-8"
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Routing) {

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
        post("/response-body") {

            val requestBody = call.receiveText()
            call.respondText(requestBody)
        }
        post("/response-body-with-model") {

            val requestBody = call.receive(RequestBody::class)
            val response = WrapperResponse(data = requestBody)
            call.respond(response)
        }
    }
}

fun main(args: Array<String>) {

    embeddedServer(Netty, 8080, watchPaths = listOf("MainKt"), module = Application::module).start()
}
