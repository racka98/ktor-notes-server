package work.racka.repository

import org.jetbrains.exposed.sql.*
import work.racka.data.NotesDatabase
import work.racka.data.model.Note
import work.racka.data.model.User
import work.racka.data.table.NoteTable
import work.racka.data.table.UserTable
import work.racka.util.Converters

class Repository(private val db: NotesDatabase) {

    suspend fun addUser(user: User) {
        db.dbQuery {
            UserTable.insert { userTable ->
                userTable[email] = user.email
                userTable[username] = user.username
                userTable[hashPassword] = user.hashPassword
            }
        }
    }

    suspend fun findUserByEmail(email: String) = db.dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { Converters.rowToUser(it) }
            .singleOrNull()

    }

    // Notes CRUD operations
    suspend fun addNote(note: Note, email: String) {
        db.dbQuery {
            NoteTable.insert { noteTable ->
                noteTable[id] = note.id
                noteTable[userEmail] = email
                noteTable[noteTitle] = note.noteTitle
                noteTable[description] = note.description
                noteTable[date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email: String): List<Note> = db.dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.mapNotNull { Converters.rowToNote(it) }
    }

    suspend fun updateNote(note: Note, email: String) {
        db.dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.id.eq(note.id) and NoteTable.userEmail.eq(email)
                }
            ) { noteTable ->
                noteTable[id] = note.id
                noteTable[userEmail] = email
                noteTable[noteTitle] = note.noteTitle
                noteTable[description] = note.description
                noteTable[date] = note.date
            }
        }
    }

    suspend fun deleteNote(id: String, email: String) {
        db.dbQuery {
            NoteTable.deleteWhere {
                NoteTable.id.eq(id) and NoteTable.userEmail.eq(email)
            }
        }
    }
}