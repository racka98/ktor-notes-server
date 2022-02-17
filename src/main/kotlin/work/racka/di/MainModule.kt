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
            driverClassName = System.getenv("JDBC_DRIVER")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            /**
             * This has been wrapped inside a try block so that we can test with
             * our local Postgres database using our local environment variable
             * Since the URLs don't match it will throw an exception on the local
             * machine and will use the local environment variable but will not throw
             * an exception in our remote server since the URL will be correct for try { }
             */
            jdbcUrl = try {
                val uri = URI(System.getenv("DATABASE_URL"))
                val userInfo = uri.userInfo.split(":").toTypedArray()
                val username = userInfo[0]
                val password = userInfo[1]
                "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
            } catch (e: Exception) {
                e.printStackTrace()
                System.getenv("DATABASE_URL")
            }

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