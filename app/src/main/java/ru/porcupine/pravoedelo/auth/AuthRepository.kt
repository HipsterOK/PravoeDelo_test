package ru.porcupine.pravoedelo.auth

import com.google.gson.Gson
import okhttp3.ResponseBody
import ru.porcupine.pravoedelo.api.ApiService
import ru.porcupine.pravoedelo.model.CodeResponse
import ru.porcupine.pravoedelo.model.ErrorResponse
import ru.porcupine.pravoedelo.network.Either

class AuthRepository(private val apiService: ApiService) {

    suspend fun getCodeWithErrorHandling(phoneNumber: String): Either<CodeResponse, ErrorResponse> {
        return try {
            Either.Success(apiService.getCode(phoneNumber))
        } catch (e: Exception) {
            Either.Failure(ErrorResponse("Ошибка валидации"))
        }
    }


    suspend fun getTokenWithErrorHandling(
        phoneNumber: String, password: String
    ): Either<ResponseBody, ErrorResponse> {
        return try {
            val response = apiService.getToken(phoneNumber, password)
            if (response.isSuccessful) {
                Either.Success(response.body()!!)
            } else {
                handleErrorResponse(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            Either.Failure(ErrorResponse("Ошибка валидации"))
        }
    }

    private fun handleErrorResponse(errorBody: String?): Either<Nothing, ErrorResponse> {
        val serverErrorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        return Either.Failure(serverErrorResponse)
    }

    suspend fun regenerateCodeWithErrorHandling(phoneNumber: String): Either<String, ErrorResponse> {
        return try {
            Either.Success(apiService.regenerateCode(phoneNumber))
        } catch (e: Exception) {
            Either.Failure(ErrorResponse("Ошибка валидации"))
        }
    }
}
