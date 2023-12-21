package ru.porcupine.pravoedelo

data class CodeResponse(
    val code: String,
    val status: String
)

data class ErrorResponse(
    val error: String
)
