package com.repository

import com.data.model.User
import com.data.table.UserTable
import com.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class Repository {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { it ->
                it[UserTable.userId] = user.userId
                it[UserTable.userEmail] = user.userEmail
                it[UserTable.userName] = user.userName
                it[UserTable.userPassword] = user.userPassword
            }
        }
    }

    suspend fun findUserByUserEmail(userEmail: String) = dbQuery {
        UserTable.select { UserTable.userEmail.eq(userEmail) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun isUserExists(userId: String): Boolean = dbQuery {
        UserTable.select { UserTable.userId.eq(userId) }
            .count() > 0
    }

    suspend fun isUserEmailExists(userEmail: String): Boolean = dbQuery {
        UserTable.select { UserTable.userEmail.eq(userEmail) }
            .count() > 0
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            userId = row[UserTable.userId],
            userName = row[UserTable.userName],
            userEmail = row[UserTable.userEmail],
            userPassword = row[UserTable.userPassword],
        )
    }

}