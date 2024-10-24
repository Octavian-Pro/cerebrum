package com.cerebrum.app.compose.phonebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.objectbox.entities.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.SortedMap

class PhonebookViewModel: ViewModel() {

  private val repository = CerebrumApp.module.phonebookRepository

  private val _contacts = MutableStateFlow<List<Contact>>(listOf())
  // val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

  private val _grouped = MutableStateFlow<Map<Char, List<Contact>>>(hashMapOf())
  val grouped: StateFlow<Map<Char, List<Contact>>> = _grouped

  private val _selected = MutableStateFlow<Contact?>(null)
  val selected: StateFlow<Contact?> = _selected.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading

  private var _filter: String? = null

  fun load() {
    viewModelScope.launch {
      repository.loadFromServer()
      _contacts.value = repository.getAll()
      updateGrouped(_filter)
    }
  }

  fun setSelected(contact: Contact) {
    _selected.value = contact
  }

  fun filter(query: String) {
    updateGrouped(query)
  }

  fun resetFilter() {
    updateGrouped(null)
  }

  fun clearSelection() {
    _selected.value = null
  }

  fun deleteContact(contact: Contact) {
    viewModelScope.launch {
      _isLoading.value = true
      try {
        repository.delete(contact.id.toInt())
        _contacts.value = _contacts.value.filter { it.id != contact.id }
        updateGrouped(_filter)
      } catch (e : Exception) {

      } finally {
        _isLoading.value = false
      }
    }
  }

  private fun getFilteredContacts(query: String?): List<Contact> {
    _filter = query
    return if (query.isNullOrBlank()) {
      _contacts.value
    } else {
      _contacts.value.filter {
        it.name.contains(query, true)
            || it.phone.replace("[+\\s]".toRegex(), "").contains(query.replace("[+\\s]".toRegex(), ""))
            || it.speciality.contains(query, true)
      }
    }
  }
  private fun updateGrouped(query: String?) {
    _grouped.value = getFilteredContacts(query)
      .filter { it.phone.isNotEmpty() }
      .groupBy { if (it.name.isEmpty()) {' '} else { it.name[0].uppercaseChar() } }
      .toSortedMap()
  }

}