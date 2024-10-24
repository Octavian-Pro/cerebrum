package com.cerebrum.app.compose.test

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.AnamnesisAnswerRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnamnesisItem(
  val id: Int,
  val question: String,
  val answer: Boolean?
)

class TestViewModel : ViewModel() {

  private val _isTestCompleted = MutableStateFlow(false)
  val isTestCompleted: StateFlow<Boolean> = _isTestCompleted.asStateFlow()

  var isLoading = mutableStateOf(false)
    private set

  var testItems = mutableStateListOf<AnamnesisItem>()
    private set

  var isAllAnswered = mutableStateOf(false)
    private set

  fun loadAnamnesisTest() {
    viewModelScope.launch {
      isLoading.value = true
      try {
        val tests = CerebrumApp.module.testRepository
          .loadAnamnesisTest()
          .getOrThrow()
        testItems.addAll(tests.map {
          AnamnesisItem(it.idTest, it.question, null)
        })
      }
      catch (e : Throwable) {
        Log.d("APPERROR", e.message ?: "Unknown error")
      }
      finally {
        isLoading.value = false
      }
    }
  }

  fun setAnswer(testId: Int, value: Boolean) {
    testItems.forEachIndexed { idx, item ->
      if (item.id == testId) {
        testItems[idx] = item.copy(answer = value)
        updateAllAnswered()
        return
      }
    }
  }

  private fun updateAllAnswered() {
    isAllAnswered.value = testItems.size > 0 && testItems.find {
      it.answer == null
    } == null
  }

  fun sendResult() {
    viewModelScope.launch {
//      val answers = listOf(
//        AnamnesisAnswerRequest(1, false),
//        AnamnesisAnswerRequest(2, true),
//        AnamnesisAnswerRequest(3, true),
//        AnamnesisAnswerRequest(4, true),
//      )
      val answers = testItems.map {
        AnamnesisAnswerRequest(
          testId = it.id,
          answer = it.answer ?: false
        )
      }
      try {
        val result = CerebrumApp.module
          .testRepository
          .updateAnamnesisTest(answers).getOrThrow()
        CerebrumApp.module
          .testRepository
          .saveUserExercises(result)
        _isTestCompleted.value = true
      }
      catch (e : Throwable) {
        Log.d("APPERROR", e.message ?: "Unknown error")
      }
    }
  }
}
