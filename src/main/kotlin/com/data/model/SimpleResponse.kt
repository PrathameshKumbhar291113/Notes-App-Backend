package com.data.model

data class SimpleResponse<T>(
    val isSuccessFull : Boolean,
    val message: String,
    val response : T? = null
)
