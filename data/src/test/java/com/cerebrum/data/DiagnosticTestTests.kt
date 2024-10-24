package com.cerebrum.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

class DiagnosticTestTests {

  private val client = ApiClient.createClient()

  fun getToken() : String = runBlocking {
    val result = client.signin(SigninRequest("adam", "qwerty123"))
    result.body()!!.accessToken!!
  }

//  @Test
//  fun diagnosticTest() {
//    val token = getToken()
//    val response = runBlocking {
//      client.getAnamnesisTest(token)
//    }
//    Assert.assertTrue(response.isSuccessful)
//  }

  @Test
  fun getDiagnosisTests() {
    val token = getToken()
    val response = runBlocking {
      client.getDiagnosticTests(token, Langs.ENG.value)
    }
    Assert.assertTrue(response.isSuccessful)
  }

  @Test
  fun postResultTest() {
    val token = getToken()
    val answers = listOf(
      DiagnosticAnswerItem(id = 1, answer = "Да"),
      DiagnosticAnswerItem(id = 2, answer = "Да"),
      DiagnosticAnswerItem(id = 3, answer = "Да"),
      DiagnosticAnswerItem(id = 4, answer = "Да"),
    )
    val response = runBlocking {
      client.sendDiagnosticTestAnswers(token, answers)
    }
    assertTrue(response.isSuccessful)
  }

  @Test
  fun diagnosticMakeFileTest() {
    val token = getToken()
    val response = runBlocking {
      client.makeFile(token)
    }
    assertTrue(response.isSuccessful)
  }
}