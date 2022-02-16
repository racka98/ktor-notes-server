package work.racka.util

import org.jetbrains.exposed.sql.ResultRow
import work.racka.data.model.Note
import work.racka.data.model.User
import work.racka.data.table.NoteTable
import work.racka.data.table.UserTable

object Converters {
    fun rowToUser(row: ResultRow?): User? {
        row?.let {
            return User(
                email = row[UserTable.email],
                username = row[UserTable.username],
                hashPassword = row[UserTable.hashPassword]
            )
        }
        return null
    }

    fun rowToNote(row: ResultRow?): Note? {
        row?.let {
            return Note(
                id = row[NoteTable.id],
                noteTitle = row[NoteTable.noteTitle],
                description = row[NoteTable.description],
                date = row[NoteTable.date]
            )
        }
        return null
    }
}