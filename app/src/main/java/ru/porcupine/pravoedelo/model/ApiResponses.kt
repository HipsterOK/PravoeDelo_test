package ru.porcupine.pravoedelo.model

data class CodeResponse(
    val code: String,
    val status: String
)

data class ErrorResponse(
    val error: String
)
