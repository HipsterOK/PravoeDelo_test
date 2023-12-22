package ru.porcupine.pravoedelo.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.porcupine.pravoedelo.api.ApiService
import ru.porcupine.pravoedelo.model.CodeResponse
import ru.porcupine.pravoedelo.model.ErrorResponse
import ru.porcupine.pravoedelo.network.Either

class AuthViewModel(apiService: ApiService) : ViewModel() {

    private val repository: AuthRepository = AuthRepository(apiService)

    private val _codeResponse = MutableLiveData<Either<CodeResponse?, ErrorResponse?>>()
    val codeResponse: LiveData<Either<CodeResponse?, ErrorResponse?>> get() = _codeResponse

    private val _regenerateCodeResponse = MutableLiveData<Either<String?, ErrorResponse?>>()
    val regenerateCodeResponse: LiveData<Either<String?, ErrorResponse?>> get() = _regenerateCodeResponse

    private val _tokenResponse = MutableLiveData<Either<ResponseBody?, ErrorResponse?>>()
    val tokenResponse: LiveData<Either<ResponseBody?, ErrorResponse?>> get() = _tokenResponse


    fun getCode(phoneNumber: String) {
        viewModelScope.launch {
            _codeResponse.value = repository.getCodeWithErrorHandling(phoneNumber)
        }
    }

    fun getToken(phoneNumber: String, password: String) {
        viewModelScope.launch {
            _tokenResponse.value = repository.getTokenWithErrorHandling(phoneNumber, password)
        }
    }

    fun regenerateCode(phoneNumber: String) {
        viewModelScope.launch {
            _regenerateCodeResponse.value = repository.regenerateCodeWithErrorHandling(phoneNumber)
        }
    }
}
