package com.data.table

import org.jetbrains.exposed.sql.Table

object NotesTable : Table() {

    val noteId = varchar("noteId", 512)
    val noteTitle = text("noteTitle")
    val noteDescription = text("noteDescription")
    val noteDate = long("noteDate")
    val userId = varchar("userId", 6).references(UserTable.userId)

    override val primaryKey: PrimaryKey = PrimaryKey(noteId)

}