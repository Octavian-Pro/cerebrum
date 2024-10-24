package com.cerebrum.app.compose.test

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.DiagnosticAnswerItem
import com.cerebrum.data.DiagnosisTestResponse
import com.cerebrum.data.DiagnosticMakeFileResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiagnosticTestViewModel: ViewModel() {

  private val _isTestCompleted = MutableStateFlow(false)
  val isTestCompleted: StateFlow<Boolean> = _isTestCompleted.asStateFlow()


  var hasPreviousResultFile = mutableStateOf(
    CerebrumApp.module.preference.getDiagnosticFileStatus()
  )
    private set

  var updateStateIsLoading = mutableStateOf(false)
    private set
  var showSendButton = mutableStateOf(false)
    private set
  var errorCode = mutableStateOf(0)
    private set
  var isLoading = mutableStateOf(false)
    private set

  var testItems = mutableStateOf<List<DiagnosisTestResponse>>(listOf())
    private set

  private var answers = hashMapOf<Int, String>() //Map<Int, String>(mapOf())

  fun load() {
    isLoading.value = true
    viewModelScope.launch {
      try {
        val response = CerebrumApp.module.testRepository
          .loadDiagnosticTest()
          .getOrThrow()
        testItems.value = response
        if (hasPreviousResultFile.value?.first?.isReady != true) {
          //makeFileRequest()
        }
      } catch (e: Throwable) {

      } finally {
        isLoading.value = false
      }
    }
  }

  fun loadState() {
    updateStateIsLoading.value = true
    viewModelScope.launch {
      try {
        makeFileRequest()
      } catch (t: Throwable) {

      } finally {
        updateStateIsLoading.value = false
      }
    }
  }

  fun updateAnswer(value: List<DiagnosticAnswerItem>) {
    value.forEach {
      answers[it.id] = it.answer
    }
    showSendButton.value = isAllAnswered()
  }

//  private fun updateTestItemCount() {
//    var result = 0
//    testItems.value.forEach {
//      result++
//      it.child?.let { child ->
//        child.subquestions.forEach {
//          result++
//        }
//      }
//    }
//    testItemsCount = result
//  }


  private fun isAllAnswered(): Boolean {
    for(item in testItems.value) {
      if (answers[item.id].isNullOrEmpty()) {
        return false
      }
      item.child?.let {child ->
        if (child.activation == answers[item.id]) {
          for (subquestion in child.subquestions) {
            if (answers[subquestion.id].isNullOrEmpty()) {
              return false
            }
          }
        }
      }
    }
    return true
  }

  private fun makeFileRequest() {
    viewModelScope.launch {
      CerebrumApp.module.testRepository
        .makeDiagnosticFile()
        .getOrThrow().let {
          val status = CerebrumApp.module.preference.setDiagnosticFileStatus(it)
          hasPreviousResultFile.value = status
        }
    }
  }

  fun submit() {
    isLoading.value = true
    // check is all answered
    viewModelScope.launch {
      try {
        val response = CerebrumApp.module.testRepository
          .sendDiagnosticTestAnswers(
            answers.toList().map {
              DiagnosticAnswerItem(it.first, it.second)
            }
          )
          .getOrThrow()
        if (response.code != 0) {
          errorCode.value = response.code
        } else {
          makeFileRequest()
          _isTestCompleted.value = true
        }
      } catch (t: Throwable) {

      } finally {
        isLoading.value = false
      }
    }
  }
}