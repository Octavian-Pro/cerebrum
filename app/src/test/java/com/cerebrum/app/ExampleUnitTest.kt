package com.cerebrum.app

import com.cerebrum.data.DiagnosticAnswerItem
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun filterTest() {

    val answers = listOf(
      DiagnosticAnswerItem(1, "1"),
      DiagnosticAnswerItem(2, "2"),
      DiagnosticAnswerItem(3, "3"),
      DiagnosticAnswerItem(4, "4"),
    )

    val values = listOf(
      //DiagnosisAnswerItem(1, "1"),
      DiagnosticAnswerItem(2, "22"),
      //DiagnosisAnswerItem(3, "3"),
      DiagnosticAnswerItem(4, "44"),
      DiagnosticAnswerItem(5, "5"),
    )

    val cleaned = answers
      .filter { current ->
        values.any { it.id == current.id }.not()
      }.toMutableList().apply {
        addAll(values)
      }
    println("=================")
    println(answers)
    println(values)
    println(cleaned)
    println("==================")
  }
}