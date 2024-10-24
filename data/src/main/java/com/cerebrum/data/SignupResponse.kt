package com.cerebrum.data

import com.squareup.moshi.Json

data class SignupResponse(
  @Json(name = "access_token")
  val accessToken: String?
)