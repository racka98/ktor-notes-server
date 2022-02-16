package work.racka.di

import com.auth0.jwt.JWT
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import work.racka.authentication.Auth
import work.racka.authentication.JwtService
import work.racka.data.NotesDatabase
import work.racka.repository.Repository
import work.racka.routes.UserRoutes

val mainModule = module {
    single {
        val config = HikariConfig()
        config.apply {
            driverClassName = System.getenv("JDBC_DRIVER")
            jdbcUrl = System.getenv("DATABASE_URL")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        HikariDataSource(config)
    }

    single { NotesDatabase(get()) }

    single { Repository(get()) }

    single {
        JWT.require(Auth.algorithm)
            .withIssuer(Auth.ISSUER)
            .build()
    }

    single { JwtService(get()) }
}