package work.racka

import io.ktor.application.*
import org.koin.ktor.ext.Koin
import work.racka.di.mainModule
import work.racka.di.routesModule
import work.racka.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        modules(mainModule, routesModule)
    }
    connectDatabase()
    configureSecurity()
    configureLocation()
    configureRouting()
    configureSerialization()
}
