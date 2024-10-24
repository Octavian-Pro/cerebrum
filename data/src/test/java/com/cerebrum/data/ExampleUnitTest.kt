package com.cerebrum.data

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

  private val client = ApiClient.createClient()

  fun getToken() : String = runBlocking {
    val result = client.signin(SigninRequest("adam", "qwerty123"))
    result.body()!!.accessToken!!
  }

  @Test
  fun addition_isCorrect() {
    runBlocking {
      val result = client.signin(SigninRequest("adam", "qwerty123"))
      assertNotNull(result.body())
      assertEquals("", result.body()!!.accessToken)
    }
  }

  @Test
  fun registrationTest() = runBlocking {
    val response = client.signup(
      SignupRequest(
        "android_test_1",
        "123456qqq",
        "firstname",
        "surname",
        "secondname",
        "2010-11-12",
        "89187778899"
      )
    )
    assertTrue(response.isSuccessful)
    val data = response.body()
    assertNotNull(data?.accessToken)
  }

  @Test
  fun getFileTest() {
    val token = getToken()
    val response = runBlocking {
      client.getUserExercises(token)
    }
    assertTrue(response.isSuccessful)
    assertTrue((response.body()?.size ?: 0) > 0)
    val fileName = response.body()!!.first().fileName
    val responseFile = runBlocking {
      client.downloadUserExercisesFile(token, fileName, "ru")
    }
    assertTrue(responseFile.isSuccessful)
  }

  @Test
  fun getArticles() {
    val token = getToken()
    val response = runBlocking {
      client.getArticles(token, Langs.RU.value)
    }
    assertTrue(response.isSuccessful)
  }

  @Test
  fun getCalendarTest() {
    val token = getToken()
    val response = runBlocking {
      client.getCalendar(token)
    }
    assertTrue(response.isSuccessful)
  }

  @Test
  fun getCalendarCategoriesTest() {
    val token = getToken()
    val response = runBlocking {
      client.getCalendarCategories(token, Langs.RU.value)
    }
    assertTrue(response.isSuccessful)
  }

  @Test
  fun addCalendarTest() {
    val token = getToken()
    val response = runBlocking {
      client.addCalendar(
        token,
        CalendarAddRequest(
          name = "текст напоминания1",
          category = "drug",
          date = "2024-10-01 4:05:06+03:00"
        )
      )
    }
    assertTrue(response.isSuccessful)
  }
  @Test
  fun testtest() {
    val count = 4
    var i = 0
    while (i < count) {
      println()
      print("ROW<")
      print("Btn${i}")
      i++
      if (i < count) {
        print(", Btn${i}")
        print(">ROW")
        i++
      }
    }
    println()

  }
}