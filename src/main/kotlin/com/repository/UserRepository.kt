package com.repository

import com.data.model.User
import com.data.table.UsersTable
import com.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {

    suspend fun addUser(user: User) {
        dbQuery {
            UsersTable.insert { it ->
                it[UsersTable.userId] = user.userId
                it[UsersTable.userEmail] = user.userEmail
                it[UsersTable.userName] = user.userName
                it[UsersTable.userPassword] = user.userPassword
            }
        }
    }

    suspend fun findUserByUserEmail(userEmail: String) = dbQuery {
        UsersTable.select { UsersTable.userEmail.eq(userEmail) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun findUserByUserId(userId: String) = dbQuery {
        UsersTable.select { UsersTable.userId.eq(userId) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun isUserExists(userId: String): Boolean = dbQuery {
        UsersTable.select { UsersTable.userId.eq(userId) }
            .count() > 0
    }

    suspend fun isUserEmailExists(userEmail: String): Boolean = dbQuery {
        UsersTable.select { UsersTable.userEmail.eq(userEmail) }
            .count() > 0
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            userId = row[UsersTable.userId],
            userName = row[UsersTable.userName],
            userEmail = row[UsersTable.userEmail],
            userPassword = row[UsersTable.userPassword],
        )
    }

}