package com.cerebrum.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming

enum class Langs(val value: String) {
  RU("ru"), ENG("eng")
}


interface ITestClient {
  @GET("API/tests/diagnostic/{lang}/pull")
  suspend fun getDiagnosticTests(
    @Header("Auth-Token") token: String,
    @Path("lang") lang: String
  ): Response<List<DiagnosisTestResponse>>

  @POST("API/tests/diagnostic/answer")
  suspend fun sendDiagnosticTestAnswers(
    @Header("Auth-Token") token: String,
    @Body answers: List<DiagnosticAnswerItem>
  ): Response<BaseCodeResponse>

  @GET("API/tests/diagnostic/answer/make_file")
  suspend fun makeFile(
    @Header("Auth-Token") token: String,
  ): Response<DiagnosticMakeFileResponse>

//  @GET("API/tests/diagnostic/answer/pull/<fileName>")
//  suspend fun getDiagnosticFile(): Response
}

interface IWebClient: ITestClient {

  @POST("signin")
  suspend fun signin(
    @Body data : SigninRequest
  ): Response<SigninResponse>

  @POST("signup")
  suspend fun signup(
    @Body request : SignupRequest
  ): Response<SignupResponse>


  @GET("API/article/{lang}/pull")
  suspend fun getArticles(
    @Header("Auth-Token") token: String,
    @Path("lang") lang: String
  ): Response<List<ArticleResponse>>

  @GET("API/tests/anamnesis/{lang}/pull")
  suspend fun getAnamnesisTest(
    @Header("Auth-Token") token: String,
    @Path("lang") lang: String
  ): Response<Array<AnamnesisItemResponse>>

  @PUT("API/user_exercises/update")
  suspend fun updateUserExercisesByTest(
    @Header("Auth-Token") token: String,
    @Body answers : List<AnamnesisAnswerRequest>
  ): Response<List<UserUpdateExerciseResponse>>

  @GET("API/user_exercises/pull")
  suspend fun getUserExercises(
    @Header("Auth-Token") token: String,
  ): Response<Array<UserExerciseItemResponse>>

  @Streaming
  @GET("API/user_exercises/file/{lang}/{fileName}")
  suspend fun downloadUserExercisesFile(
    @Header("Auth-Token") token: String,
    @Path("fileName") fileName: String,
    @Path("lang") language: String,
  ): Response<ResponseBody>

  // Phonebook
  @GET("API/phonebook/list/get")
  suspend fun getPhonebook(
    @Header("Auth-Token") token: String,
  ) : Response<List<GetPhonebookResponse>>

  @POST("API/phonebook/add")
  suspend fun addPhonebookContact(
    @Header("Auth-Token") token: String,
    @Body data: PhonebookContactRequest
  ): Response<AddPhonebookContactResponse>

  @DELETE("API/phonebook/delete/{id}")
  suspend fun deletePhonebookContact(
    @Header("Auth-Token") token: String,
    @Path("id") id: Int
  ): Response<DeletePhonebookContactResponse>

  @GET("API/calendar/pull")
  suspend fun getCalendar(
    @Header("Auth-Token") token: String,
  ): Response<List<CalendarResponse>>

  @GET("API/calendar/event_types/{lang}/pull")
  suspend fun getCalendarCategories(
    @Header("Auth-Token") token: String,
    @Path("lang") language: String,
  ): Response<List<CalendarCategoryResponse>>
  @POST("/API/calendar/add")
  suspend fun addCalendar(
    @Header("Auth-Token") token: String,
    @Body data: CalendarAddRequest
  ): Response<BaseIdResponse<String>>
  @DELETE("/API/calendar/delete/{uid}")
  suspend fun deleteCalendar(
    @Header("Auth-Token") token: String,
    @Path("uid") uid: String
  ): Response<BaseCodeResponse>
}