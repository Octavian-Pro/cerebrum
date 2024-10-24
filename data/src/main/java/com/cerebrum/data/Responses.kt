package com.cerebrum.data

import com.squareup.moshi.Json


data class AnamnesisItemResponse(
  @Json(name = "id_test_anamnesis")
  val idTest: Int,
  @Json(name = "question")
  val question : String
)

data class ArticleResponse(
  @Json(name = "articles_name")
  val name: String,
  @Json(name = "filename")
  val fileName: String,
  @Json(name = "file_extension")
  val fileExtension: String
  /*
[
{
  "articles_name": "\u0427\u0442\u043e \u0442\u0430\u043a\u043e\u0435 \u0438\u043d\u0441\u0443\u043b\u044c\u0442",
  "filename": "\u0427\u0442\u043e \u0442\u0430\u043a\u043e\u0435 \u0438\u043d\u0441\u0443\u043b\u044c\u0442",
  "file_extension": "htm"
}, {"articles_name": "\u041f\u0441\u0438\u0445\u043e\u043b\u043e\u0433\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430", "filename": "\u041f\u0441\u0438\u0445\u043e\u043b\u043e\u0433\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430", "file_extension": "htm"}, {"articles_name": "\u041f\u0440\u0438\u0447\u0438\u043d\u044b \u0438 \u043f\u0440\u043e\u0444\u0438\u043b\u0430\u043a\u0442\u0438\u043a\u0430", "filename": "\u041f\u0440\u0438\u0447\u0438\u043d\u044b \u0438 \u043f\u0440\u043e\u0444\u0438\u043b\u0430\u043a\u0442\u0438\u043a\u0430", "file_extension": "htm"}
]
  *
  * */
)

data class UserUpdateExerciseResponse(
  @Json(name = "id_exercises")
  val exercisesId: Int,
  @Json(name = "filename")
  val fileName: String,
  @Json(name = "exercises_name")
  val exercisesName: String,
  @Json(name = "file_extension")
  val fileExtension: String,
  @Json(name = "link")
  val videoLink: String?, // youtube
  @Json(name = "lang")
  val lang: List<String>
)

data class UserExerciseItemResponse(
  @Json(name = "exercises_name")
  val exerciseName: String,
  @Json(name = "filename")
  val fileName: String
)

data class GetPhonebookResponse (
  @Json(name = "id")
  val id: Int,
  @Json(name = "contact_name")
  val name: String,
  @Json(name = "phone_number")
  val phone: String,
  @Json(name = "speciality")
  val speciality: String
)

object ServerCalendarEventType {
  const val event = "event"
  const val exercise = "exercise"
  const val drug = "drug"
}

data class CalendarResponse(
  @Json(name = "id")
  val id: String,
  @Json(name = "name")
  val name: String,
  //“category”: <EventType>
  @Json(name = "category")
  val category: String?,
  //“datetime”: <дата-время в iso формате> exm: "2023-10-10 12:13:00+00:00"
  @Json(name = "datetime")
  val date: String,
)

data class CalendarCategoryResponse(
  @Json(name = "id")
  val id: Int,
  @Json(name = "category")
  val category: String,
  @Json(name = "caption")
  val caption: String,
)

data class BaseIdResponse<T>(
  val id: T
)
data class BaseCodeResponse(
  val code: Int
)

data class AddPhonebookContactResponse(
  val id: Int
)

data class DeletePhonebookContactResponse(
  val code: Int
)
/*
  {
    "id": 18,
    "question": "У вас есть проблемы с мочеиспусканием?",
    "npp": 18,
    "answers_variant": [
      "Нет",
      "Да"
    ],
    "child": [
      {
        "id": 23,
        "question": "Вы страдаете от затрудненного мочеиспускания?",
        "npp": 2,
        "variants": [
          "Нет",
          "Да"
        ]
      },
      {
        "id": 22,
        "question": "Вы страдаете от недержания мочи?",
        "npp": 1,
        "variants": [
          "Нет",
          "Да"
        ]
      }
    ]
  },
* */
data class DiagnosisTestResponse(
  @Json(name = "id")
  val id: Int = 0,
  @Json(name = "npp")
  val number: Int = 0,
  @Json(name = "question")
  val question: String,
  @Json(name = "answers_variant")
  val answers: List<String>?,
  @Json(name = "child")
  val child: DiagnosisTestChild?
)


data class DiagnosisTestChild(
  @Json(name = "activation")
  val activation: String = "",
  @Json(name = "subquestions")
  val subquestions: List<DiagnosisTest>
)

data class DiagnosisTest(
  @Json(name = "id")
  val id: Int = 0,
  @Json(name = "npp")
  val number: Int = 0,
  @Json(name = "question")
  val question: String,
  @Json(name = "variants")
  val answers: List<String>?,
)

data class DiagnosticMakeFileResponse(
  @Json(name = "file_name") // string -- имя файла с документом в хранилище
  val fileName: String,
  @Json(name = "ready") //: bool – признак готовности файл
  val isReady: Boolean
)