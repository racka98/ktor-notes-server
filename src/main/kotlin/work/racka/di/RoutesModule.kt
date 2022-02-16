package work.racka.di

import org.koin.dsl.module
import work.racka.authentication.Auth
import work.racka.routes.NoteRoutes
import work.racka.routes.UserRoutes

val routesModule = module {
    single {
        UserRoutes(
            dbRepo = get(),
            jwtService = get(),
            hashFunction = { password ->
                Auth.hash(password)
            }
        )
    }

    single {
        NoteRoutes(
            dbRepo = get()
        )
    }
}