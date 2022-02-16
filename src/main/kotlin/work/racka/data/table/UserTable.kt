package work.racka.data.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val email = varchar("email", 512)
    val username = varchar("username", 512)
    val hashPassword = varchar("hashPassword", 512)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(email)
}