package com.cerebrum.data.repositories

import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.SigninRequest
import com.cerebrum.data.SigninResponse
import com.cerebrum.data.SignupRequest
import com.cerebrum.data.SignupResponse

class AuthRepository(
  private val apiClient: IWebClient,
  private val authStore: IAuthStore
) : BaseRepository() {

  suspend fun signup(
    login : String,
    password: String,
    firstName: String,
    surname: String, // отчество?
    secondName: String,
    birthDate: String,
    phone: String
  ) : Result<SignupResponse> {

    val result = execute {
      apiClient
        .signup(SignupRequest(
          login,
          password,
          firstName,
          surname,
          secondName,
          birthDate,
          phone)
        )
    }
    if (result.isSuccess) {
      return try {
        val token = result.getOrThrow()
        token.accessToken?.let {
          authStore.setToken(it)
          authStore.setLogin(login)
        }
        Result.success(token)
      }
      catch (e : Throwable) {
        Result.failure(e)
      }
    }
    return result
  }

  suspend fun signin(
    login: String,
    password: String
  ) : Result<SigninResponse> {
    val result = execute {
      apiClient.signin(SigninRequest(login, password))
    }
    if (result.isSuccess) {
      return try {
        val token = result.getOrThrow()
        token.accessToken?.let {
          authStore.setToken(it)
          authStore.setLogin(login)
        }
        Result.success(token)
      }
      catch(e : Throwable) {
        return Result.failure(e)
      }
    }
    return result
  }

}