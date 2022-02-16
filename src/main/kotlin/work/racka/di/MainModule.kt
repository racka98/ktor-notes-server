package work.racka.di

import com.auth0.jwt.JWT
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import work.racka.authentication.Auth
import work.racka.authentication.JwtService
import work.racka.data.NotesDatabase
import work.racka.repository.Repository
import java.net.URI

val mainModule = module {
    single {
        val config = HikariConfig()
        config.apply {
            val uri = URI(System.getenv("DATABASE_URL"))
            val userInfo = uri.userInfo.split(":").toTypedArray()
            val username = userInfo[0]
            val password = userInfo[1]

            driverClassName = System.getenv("JDBC_DRIVER")
            //jdbcUrl = System.getenv("DATABASE_URL") For Local Use
            jdbcUrl =
                "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
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