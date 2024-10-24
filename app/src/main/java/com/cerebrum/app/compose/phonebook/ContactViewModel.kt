package com.cerebrum.app.compose.phonebook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.CommonErrors
import com.cerebrum.app.EnumError
import com.cerebrum.app.ErrorsViewModel
import com.cerebrum.app.compose.formatPhone
import com.cerebrum.app.compose.isValidPhone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


enum class ContactViewErrors: EnumError {
  INCORRECT_PHONE
}

class Field(
  initialValue: String = "",
  val isRequired: Boolean = false,
  val processValue: (value: String) -> String = { arg -> arg },
  val errorsCollector: Array<(value: String) -> EnumError?> //(Array<(predicates: () -> EnumError?)>) -> List<EnumError>,
) {

  private var isTouched = false

  var value by mutableStateOf(initialValue)
    private set

  var errors by mutableStateOf(mutableListOf<EnumError>())
    private set
  fun update(value: String) {
    this.isTouched = true
    this.value = processValue(value)
    collectErrors()
  }

  fun collectErrors() {
    errors.clear()
    errorsCollector.forEach {
      it(this.value)?.let { error ->
        errors.add(error)
      }
    }
  }

  val hasError: Boolean get() = errors.isNotEmpty()

}

fun isEmptyField(value: String?): EnumError? {
  return if (value.isNullOrEmpty()) {
    CommonErrors.EMPTY_FIELDS
  } else
    null
}

data class Form(
  val name: Field = Field(
    isRequired = true,
    errorsCollector = arrayOf(
      ::isEmptyField
    )
  ),
  val phone: Field = Field(
    isRequired = true,
    processValue = {
      formatPhone(it)
    },
    errorsCollector = arrayOf(
      ::isEmptyField,
      { value ->
        return@arrayOf if (!isValidPhone(value))
          ContactViewErrors.INCORRECT_PHONE
        else
          null
      }
    )
  ),
  val speciality: Field = Field(
    isRequired = true,
    errorsCollector = arrayOf(::isEmptyField)
  )
) {

  private var errors: List<EnumError> = emptyList()

  private fun appendError(error : EnumError) {
    errors = errors.toMutableList().apply {
      if (!contains(error)) {
        add(error)
      }
    }
  }

  private fun clearErrors() {
    errors = emptyList()
  }

  private fun removeError(value : EnumError) {
    errors = errors.filter { it != value }
  }

  fun collectErrors() {
    name.collectErrors()
    phone.collectErrors()
    speciality.collectErrors()
  }
  fun isNoErrors(): Boolean {
    return errors.isEmpty()
        && name.errors.isEmpty()
        && phone.errors.isEmpty()
        && speciality.errors.isEmpty()
  }
}

data class AddContactState(
  val isLoading: Boolean = false,
  // val form: Form = Form(),
  val errors: List<EnumError> = emptyList(),
  val isSaved: Boolean = false
)

class ContactViewModel(
  state: SavedStateHandle
): ErrorsViewModel() {

  private val contactId = checkNotNull(state.get<Long>("id"))

  private val repository = CerebrumApp.module.phonebookRepository

  private val _state = MutableStateFlow(AddContactState())
  val state: StateFlow<AddContactState> = _state.asStateFlow()

  var name by mutableStateOf("")
    //private set
  var nameIsError by mutableStateOf(false)
    private set

  var phone by mutableStateOf("")
    //private set
  var phoneIsError by mutableStateOf(false)
    private set

  var speciality by mutableStateOf("")
    //private set
  var specialityIsError by mutableStateOf(false)
    private set

//  init {
//    if (contactId > 0L) {
//      repository.get(contactId)?.let {
//        name = it.name
//        phone = it.phone
//        speciality = it.speciality
//      }
//    }
//  }

  fun isNew() = contactId == 0L

  private fun checkErrors() : Boolean {
    clearErrors()
    var hasEmptyFields = false
    if (name.isEmpty()) {
      hasEmptyFields = true
      nameIsError = true
    }
    if (speciality.isEmpty()) {
      hasEmptyFields = true
      specialityIsError = true
    }
    if (phone.isEmpty()) {
      hasEmptyFields = true
      phoneIsError = true
    }
    if (!phoneIsError) {
      if (!isValidPhone(phone)) {
        phoneIsError = true
        appendError(ContactViewErrors.INCORRECT_PHONE)
      }
    }
    if (hasEmptyFields) {
      appendError(CommonErrors.EMPTY_FIELDS)
    }
    return !hasErrors()
  }

  fun submit() {
    // val form = state.value.form

    if (checkErrors()) {
      _state.update {
        it.copy(isLoading = true)
      }
      viewModelScope.launch {
        try {
          val result = repository.add(
            name,
            phone.replace("[+\\-\\s]".toRegex(), ""),
            speciality
          ).getOrThrow()
          if (result.id > 0) {
            _state.update {
              it.copy(isLoading = false, isSaved = true)
            }
          }
        }
        catch (e : Throwable) {
          appendError(CommonErrors.SERVER_ERROR)
        }
        finally {
          _state.update {
            it.copy(isLoading = false)
          }
        }
      }
    }
  }
}