package ru.porcupine.pravoedelo.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.porcupine.pravoedelo.model.CodeResponse

interface ApiService {

    @GET("getCode")
    suspend fun getCode(@Query("login") phoneNumber: String): CodeResponse

    @GET("getToken")
    suspend fun getToken(
        @Query("login") phoneNumber: String, @Query("password") password: String
    ): Response<ResponseBody>

    @GET("regenerateCode")
    suspend fun regenerateCode(@Query("login") phoneNumber: String): String
}
