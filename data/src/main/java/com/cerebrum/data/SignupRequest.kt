package com.cerebrum.data

import com.squareup.moshi.Json

data class SignupRequest(
  @Json(name = "login")
  val login: String,
  @Json(name = "password")
  val password: String,
  @Json(name = "first_name")
  val firstName: String,
  @Json(name = "surname")
  val surname: String,
  @Json(name = "second_name")
  val secondName: String,
  @Json(name = "date_birth")
  val birthDate: String,// "2001-10-10",
  @Json(name = "phone_number")
  val phone: String, // "8923133215"
)