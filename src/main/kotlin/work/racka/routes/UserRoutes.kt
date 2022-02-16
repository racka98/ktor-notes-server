package work.racka.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import work.racka.authentication.JwtService
import work.racka.data.model.LoginRequest
import work.racka.data.model.RegisterRequest
import work.racka.data.model.SimpleResponse
import work.racka.data.model.User
import work.racka.repository.Repository
import work.racka.util.Constants

class UserRoutes(
    private val dbRepo: Repository,
    private val jwtService: JwtService,
    private val hashFunction: (String) -> String
) {

    fun Route.routes() {
        post<UserRegisterRoute> {
            val registerRequest = try {
                call.receive<RegisterRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, Constants.ERROR_MISSING_FIELDS)
                )
                return@post
            }

            try {
                val user = User(
                    email = registerRequest.email,
                    hashPassword = hashFunction(registerRequest.password),
                    username = registerRequest.username
                )
                dbRepo.addUser(user)
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(true, jwtService.generateToken(user))
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(false, e.message ?: Constants.ERROR_GENERIC)
                )
            }
        }

        post<UserLoginRoute> {
            val loginRequest = try {
                call.receive<LoginRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, Constants.ERROR_MISSING_FIELDS)
                )
                return@post
            }

            try {
                val user = dbRepo.findUserByEmail(loginRequest.email)
                if (user == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, Constants.ERROR_BAD_EMAIL)
                    )
                } else {
                    if (user.hashPassword == hashFunction(loginRequest.password)) {
                        call.respond(
                            HttpStatusCode.OK,
                            SimpleResponse(true, jwtService.generateToken(user))
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            SimpleResponse(false, Constants.ERROR_BAD_PASSWORD)
                        )
                    }
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(false, e.message ?: Constants.ERROR_GENERIC)
                )
            }
        }
    }

    @Location(Constants.REGISTER_REQUEST)
    class UserRegisterRoute

    @Location(Constants.LOGIN_REQUEST)
    class UserLoginRoute
}
