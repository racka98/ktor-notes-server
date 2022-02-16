package work.racka.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject
import work.racka.authentication.Auth
import work.racka.authentication.JwtService
import work.racka.data.model.User
import work.racka.repository.Repository
import kotlin.collections.set

fun Application.configureSecurity() {

    val jwtService by inject<JwtService>()
    val dbRepo by inject<Repository>()
    val hashFunction = { s: String -> Auth.hash(s) }

    authentication {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = environment.config.property("jwt.realm").getString()
            validate { credential ->
                val payload = credential.payload
                val email = payload.getClaim("email").asString()
                val user = dbRepo.findUserByEmail(email)
                user
            }
        }
    }

    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {

        get("/token") {
            val email = call.request.queryParameters["email"]
            val password = call.request.queryParameters["password"]
            val username = call.request.queryParameters["username"]

            val user = User(
                email = email!!,
                hashPassword = hashFunction(password!!),
                username = username!!
            )
            call.respond(jwtService.generateToken(user))
        }

        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
