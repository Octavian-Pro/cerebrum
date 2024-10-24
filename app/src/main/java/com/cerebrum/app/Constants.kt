package com.cerebrum.app

import com.cerebrum.app.compose.login.LoginErrors
import com.cerebrum.app.compose.phonebook.ContactViewErrors
import com.cerebrum.app.compose.registration.RegistrationErrors

const val QUESTION_EMAIL = "question.mail.stroke@gmail.com"

enum class CommonErrors: EnumError {
  EMPTY_FIELDS,
  SERVER_ERROR,
  INCORRECT_PHONE,
}

val ErrorsTexts = mapOf(
  CommonErrors.SERVER_ERROR to "Ошибка сервера",
  CommonErrors.EMPTY_FIELDS to "Все поля обязательны для заполнения",
  CommonErrors.INCORRECT_PHONE to "Некорректный номер телефона",
  //
  // RegistrationErrors.EMPTY_FIELDS to "Все поля обязательны для заполнения",
  RegistrationErrors.INCORRECT_EMAIL to "Некорректный Email",
  RegistrationErrors.PASSWORDS_NOT_EQUALS to "Пароли не совпадают",
  RegistrationErrors.REGISTRATION_ERROR to "Ошибка при регистрации",
  //
  //
  LoginErrors.INCORRECT_LOGIN to "Неверный логин или пароль",
  LoginErrors.INCORRECT_EMAIL to "Некорректный email",
)