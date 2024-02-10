package com.data.model

data class UserLoginResponse(
    val user: UserResponseWithoutPassword,
    val token: String
)
