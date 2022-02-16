package work.racka.plugins

import io.ktor.application.*
import org.koin.ktor.ext.inject
import work.racka.data.NotesDatabase

fun Application.connectDatabase() {
    val db by inject<NotesDatabase>()
    db.connect()
}