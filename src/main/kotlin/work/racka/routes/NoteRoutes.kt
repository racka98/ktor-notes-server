package work.racka.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import work.racka.data.model.Note
import work.racka.data.model.SimpleResponse
import work.racka.data.model.User
import work.racka.repository.Repository
import work.racka.util.Constants

class NoteRoutes(
    private val dbRepo: Repository
) {

    fun Route.routes() {
        authenticate("jwt") {
            post<NoteCreateRoute> {
                val note = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, Constants.ERROR_MISSING_FIELDS)
                    )
                    return@post
                }

                try {
                    val email = call.principal<User>()!!.email
                    dbRepo.addNote(note, email)
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(true, Constants.SUCCESS_NOTE_ADDED)
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: Constants.ERROR_GENERIC)
                    )
                }
            }

            get<NoteGetRoute> {
                try {
                    val email = call.principal<User>()!!.email
                    val notes = dbRepo.getAllNotes(email)
                    call.respond(HttpStatusCode.OK, notes)
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        emptyList<Note>()
                    )
                }
            }

            post<NoteUpdateRoute> {
                val note = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, Constants.ERROR_MISSING_FIELDS)
                    )
                    return@post
                }

                try {
                    val email = call.principal<User>()!!.email
                    dbRepo.updateNote(note, email)
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(true, Constants.SUCCESS_NOTE_UPDATED)
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: Constants.ERROR_GENERIC)
                    )
                }
            }

            delete<NoteDeleteRoute> {
                val noteId = try {
                    call.parameters["id"]!!
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, Constants.ERROR_PARAMETER_ID_NOT_PRESENT)
                    )
                    return@delete
                }

                try {
                    val email = call.principal<User>()!!.email
                    dbRepo.deleteNote(noteId, email)
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(true, Constants.SUCCESS_NOTE_DELETED)
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: Constants.ERROR_GENERIC)
                    )
                }
            }
        }
    }

    @Location(Constants.NOTES)
    class NoteGetRoute

    @Location(Constants.CREATE_NOTE)
    class NoteCreateRoute

    @Location(Constants.UPDATE_NOTE)
    class NoteUpdateRoute

    @Location(Constants.DELETE_NOTE)
    class NoteDeleteRoute
}