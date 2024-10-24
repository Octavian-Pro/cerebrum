package com.cerebrum.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cerebrum.app.compose.login.LoginErrors

interface EnumError

abstract class ErrorsViewModel: ViewModel() {

  var errors by mutableStateOf<List<EnumError>>(emptyList())
    private set

  protected fun appendError(error : EnumError) {
    errors = errors.toMutableList().apply {
      if (!contains(error)) {
        add(error)
      }
    }
  }

  protected fun clearErrors() {
    errors = emptyList()
  }

  protected fun removeError(value : EnumError) {
    errors = errors.filter { it != value }
  }

  fun hasErrors() = false //errors.isNotEmpty()
}