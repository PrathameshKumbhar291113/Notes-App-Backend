package com.repository

import com.data.model.Notes
import com.data.table.NotesTable
import com.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class NotesRepository {

    suspend fun addNote(note: Notes, userId: String) {
        dbQuery {
            NotesTable.insert { it ->
                it[noteId] = note.noteId
                it[NotesTable.userId] = userId
                it[noteTitle] = note.noteTitle
                it[noteDescription] = note.noteDescription
                it[noteDate] = note.noteDate
            }
        }
    }

    suspend fun getAllNotes(userId: String): List<Notes> = dbQuery {
        NotesTable.select {
            NotesTable.userId.eq(userId)
        }.mapNotNull { rowToNotes(it) }
    }

    suspend fun updateNotes(note: Notes, userId: String){
       dbQuery {
           NotesTable.update(
               where = {NotesTable.userId.eq(userId) and NotesTable.noteId.eq(note.noteId) }
           ) {
               it[noteTitle] = note.noteTitle
               it[noteDescription] = note.noteDescription
               it[noteDate] = note.noteDate
           }
       }
    }

    suspend fun deleteNotes(noteId : String, userId: String){
        dbQuery {
            NotesTable.deleteWhere {
                NotesTable.noteId.eq(noteId) and NotesTable.userId.eq(userId)
            }
        }
    }

    private fun rowToNotes(row: ResultRow?): Notes? {
        if (row == null) {
            return null
        }
        return Notes(
            noteId = row[NotesTable.noteId],
            noteTitle = row[NotesTable.noteTitle],
            noteDescription = row[NotesTable.noteDescription],
            noteDate = row[NotesTable.noteDate]
        )
    }

}