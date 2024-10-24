package com.cerebrum.data.repositories

import retrofit2.Response

abstract class BaseRepository {
  protected suspend fun <T> execute(
    call : suspend () -> Response<T>
  ) : Result<T> {
    val response = call()
    if (response.isSuccessful) {
      response.body()?.let {
        return Result.success(response.body()!!)
      }
    }
    return Result.failure(Exception("Response error"))
  }
}