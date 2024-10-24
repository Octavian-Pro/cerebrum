package com.cerebrum.app.compose.registration

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.CommonErrors
import com.cerebrum.app.EnumError
import com.cerebrum.app.ErrorsViewModel
import com.cerebrum.app.compose.formatPhone
import com.cerebrum.app.compose.isValidPhone
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class RegistrationErrors: EnumError {
  INCORRECT_EMAIL,
  PASSWORDS_NOT_EQUALS,
  REGISTRATION_ERROR,
}

class RegistrationViewModel : ErrorsViewModel() {

  var isLoading by mutableStateOf(false)
    private set

  var isSuccess by mutableStateOf(false)
    private set

  var secondName by mutableStateOf("")
  var secondNameIsError by mutableStateOf(false)
  fun setSecondNameValue(value : String) {
    secondName = value
    secondNameIsError = secondName.isEmpty()
  }

  var surname by mutableStateOf("")
  var surnameIsError by mutableStateOf(false)
  fun setSurnameValue(value : String) {
    surname = value
    surnameIsError = surname.isEmpty()
  }

  var name by mutableStateOf("")
  var nameIsError by mutableStateOf(false)
  fun setNameValue(value : String) {
    name = value
    nameIsError = name.isEmpty()
  }
/*
  var email by mutableStateOf("")
  var emailIsError by mutableStateOf(false)
  fun setEmailValue(value : String) {
    email = value
    val isCorrect = isEmailCorrect()
    emailIsError = !isCorrect
  }
*/
  var login by mutableStateOf("")
  var loginIsError by mutableStateOf(false)
  fun setLoginValue(value : String) {
    login = value
    loginIsError = login.isEmpty()
  }

  var phone by mutableStateOf("")
  var phoneIsError by mutableStateOf(false)
  fun setPhoneValue(value: String) {
    phone = formatPhone(value)
    phoneIsError = isValidPhone(value)
  }

  var password by mutableStateOf("")
  var passwordIsError by mutableStateOf(false)
  fun setPasswordValue(value : String) {
    password = value
    passwordIsError = password.isEmpty()
  }

  var passwordRe by mutableStateOf("")
  var passwordReIsError by mutableStateOf(false)
  fun setPasswordReValue(value : String) {
    passwordRe = value
    passwordReIsError = passwordRe.isEmpty()
  }

  var birthDate by mutableStateOf<LocalDate>(LocalDate.now())
  var birthDateIsError by mutableStateOf(false)

  fun formattedDate() : String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return formatter.format(birthDate)
  }

  fun submit() {
    isLoading = true
    viewModelScope.launch {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      try {
        CerebrumApp.module.authRepository.signup(
          login = login,
          password,
          name,
          surname, // отчество?
          secondName,
          birthDate = formatter.format(birthDate),
          phone.replace("[+\\-\\s]".toRegex(), ""),
        ).getOrThrow()
        isSuccess = true
      }
      catch (e : Throwable) {
        appendError(RegistrationErrors.REGISTRATION_ERROR)
      }
      finally {
        isLoading = false
      }
    }
  }

//  private fun checkEmail() {
//    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//      appendError(RegistrationErrors.INCORRECT_EMAIL)
//    }
//  }
//  private fun isEmailCorrect() : Boolean {
//    return (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
//  }

  private fun checkPhone() {
    if (!isValidPhone(phone)) {
      phoneIsError = true
      appendError(CommonErrors.INCORRECT_PHONE)
    }
  }
  private fun checkPasswords() {
    if (password != passwordRe) {
      appendError(RegistrationErrors.PASSWORDS_NOT_EQUALS)
    }
  }

  fun setBirthday(dateInMs : Long) {
    birthDate = Instant
      .ofEpochMilli(dateInMs)
      .atZone(ZoneId.systemDefault())
      .toLocalDate()
  }

  fun checkLoginPage() {
    clearErrors()
    var hasErrors = false
//    if (email.isEmpty()) {
//      emailIsError = true
//      hasErrors = true
//    }
    if (login.isEmpty()) {
      loginIsError = true
      hasErrors = true
    }
    if (password.isEmpty()) {
      passwordIsError = true
      hasErrors = true
    }
    if (passwordRe.isEmpty()) {
      passwordReIsError = true
      hasErrors = true
    }
    if (phone.isEmpty()) {
      phoneIsError = true
      hasErrors = true
    }
    if (hasErrors) {
      appendError(CommonErrors.EMPTY_FIELDS)
    }
    checkPasswords()
    checkPhone()
    //checkEmail()
  }

  fun checkPersonalDataPage() {
    clearErrors()
    var hasErrors = false
    if (secondName.isEmpty()) {
      secondNameIsError = true
      hasErrors = true
    }
    if (surname.isEmpty()) {
      surnameIsError = true
      hasErrors = true
    }
    if (name.isEmpty()) {
      nameIsError = true
      hasErrors = true
    }
    if (hasErrors) {
      appendError(CommonErrors.EMPTY_FIELDS)
    }
  }

}