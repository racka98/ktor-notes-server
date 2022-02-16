package work.racka.plugins

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import work.racka.routes.NoteRoutes
import work.racka.routes.UserRoutes

fun Application.configureRouting() {

    val userRoutes by inject<UserRoutes>()
    val noteRoutes by inject<NoteRoutes>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes.run { routes() }

        noteRoutes.run { routes() }

        get("/note/{id}") {
            val id = call.parameters["id"]
            id?.let {
                call.respond(it)
            }
        }

        get("/note") {
            val id = call.request.queryParameters["id"]
            id?.let {
                call.respond(it)
            }
        }

        route("/notes") {
            route("/create") {
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }

            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }

    }
}
