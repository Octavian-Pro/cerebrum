package com.cerebrum.app.compose.login

import android.os.Build
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.BuildConfig
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.CommonErrors
import com.cerebrum.app.EnumError
import com.cerebrum.app.ErrorsViewModel
import com.cerebrum.app.ui.theme.CerebrumTheme
import kotlinx.coroutines.launch

enum class LoginErrors : EnumError {
  INCORRECT_EMAIL,
  INCORRECT_LOGIN
}

enum class LoginEvents {
  LOGIN_SUCCESS
}

class LoginViewModel : ErrorsViewModel() {

  private var _events = MutableLiveData<LoginEvents>()
  val events : LiveData<LoginEvents> get() = _events

  var isLoading by mutableStateOf(false)
    private set

  var email by mutableStateOf("") // LOGIN!
  var emailIsError by mutableStateOf(false)
  fun setEmailValue(value : String) {
    email = value
  }

  var password by mutableStateOf("")
  var passwordIsError by mutableStateOf(false)
  fun setPasswordValue(value : String) {
    password = value
  }

  init {
    if (BuildConfig.DEBUG) {
      email = "adam"
      password = "qwerty123"
    }
  }

  fun submit() {
    clearErrors()
    checkEmptyFields()
    // checkEmail()
    if (!hasErrors()) {
      isLoading = true
      viewModelScope.launch {
        //_events.postValue(LoginEvents.LOGIN_SUCCESS)
        try {
          val result = CerebrumApp
            .module
            .authRepository
            .signin(email, password)
            .getOrThrow()
          if (result.accessToken != null) {
            _events.postValue(LoginEvents.LOGIN_SUCCESS)
            // подгурзка уведомлений
            // подгрузка категорий уведомлений
            CerebrumApp.module.loadEvents()
            // загрузка результатов теста
            CerebrumApp.module.preference.clearDiagnosticFileStatus()
          } else {
            appendError(LoginErrors.INCORRECT_LOGIN)
          }
        }
        catch (e: Throwable) {
          appendError(LoginErrors.INCORRECT_LOGIN)
        }
        finally {
          isLoading = false
        }
      }
    }
  }

  private suspend fun loadEvents() {

  }

  private fun checkEmptyFields() {
    var hasError = false
    if (email.isEmpty()) {
      emailIsError = true
      hasError = true
    }
    if (password.isEmpty()) {
      passwordIsError = true
      hasError = true
    }
    if (hasError) {
      appendError(CommonErrors.EMPTY_FIELDS)
    } else {
      removeError(CommonErrors.EMPTY_FIELDS)
    }
  }

  fun checkEmail() {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      appendError(LoginErrors.INCORRECT_EMAIL)
    } else {
      removeError(LoginErrors.INCORRECT_EMAIL)
    }
  }

}