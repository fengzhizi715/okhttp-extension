import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.text.DateFormat

/**
 *
 * @FileName:
 *          .Main
 * @author: Tony Shen
 * @date: 2020-10-11 02:02
 * @version: V1.0 <描述当前版本功能>
 */
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
        post("/testPost") {

            val requestBody = call.receiveText()
            call.respondText(requestBody)
        }
    }
}

fun main(args: Array<String>) {

    embeddedServer(Netty, 8080, watchPaths = listOf("MainKt"), module = Application::module).start()
}
