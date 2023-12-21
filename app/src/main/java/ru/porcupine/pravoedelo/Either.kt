package ru.porcupine.pravoedelo

sealed class Either<out A, out B> {
    data class Success<out A>(val value: A) : Either<A, Nothing>()
    data class Failure<out B>(val error: B) : Either<Nothing, B>()
}