package com.data.model

import io.ktor.server.auth.*


data class User(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userPassword: String
): Principal
