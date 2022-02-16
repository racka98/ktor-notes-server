package work.racka.plugins

import io.ktor.application.*
import io.ktor.locations.*

fun Application.configureLocation() {
    install(Locations)
}