package com.data.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {

    val userId = integer("userId").autoIncrement()
    val userName = varchar("userName", 512)
    val userEmail = varchar("userEmail", 512)
    val userPassword = varchar("userPassword", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)

}