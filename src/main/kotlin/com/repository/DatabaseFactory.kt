package com.repository

import com.data.table.NotesTable
import com.data.table.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun initializationOfDb(){
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(NotesTable)
        }
    }

    private fun hikari():HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql:notes_db?user=postgres&password=Pratham13"
        config.maximumPoolSize = 3
        config.isAutoCommit = true
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T) :T =
        withContext(Dispatchers.IO){
            transaction{block()}
        }

}