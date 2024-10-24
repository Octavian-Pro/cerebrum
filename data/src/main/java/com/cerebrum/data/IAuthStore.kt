package com.cerebrum.data

interface IAuthStore {
  fun setToken(value : String)
  fun getToken() : String

  fun setLogin(value: String)
  fun getLogin(): String

  fun clearAuth()

  fun hasCredentials(): Boolean

  fun getLang(): Langs

}