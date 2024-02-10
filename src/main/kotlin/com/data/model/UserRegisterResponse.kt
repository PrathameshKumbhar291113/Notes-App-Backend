package com.data.model

data class UserRegisterResponse(
    val user: UserResponseWithoutPassword,
    val token: String
)
