package com.cerebrum.data

import com.squareup.moshi.Json

data class AnamnesisAnswerRequest(
  @Json(name = "id_test_anamnesis")
  val testId: Int,
  @Json(name = "answer")
  val answer: Boolean
)

data class PhonebookContactRequest(
  @Json(name = "contact_name")
  val name: String,
  @Json(name = "phone_number")
  val phone: String,
  @Json(name = "speciality")
  val speciality: String
)

data class CalendarAddRequest(
  @Json(name = "name")
  val name: String?,
  @Json(name = "category")
  val category: String,
  @Json(name = "datetime") // yyyy-MM-dd HH:mm:ss+00:00
  val date: String
)

data class DiagnosticAnswerItem(
  @Json(name = "id_test_diagnostic")
  val id: Int,
  @Json(name = "answer")
  val answer: String
)