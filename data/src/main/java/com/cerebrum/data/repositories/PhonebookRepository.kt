package com.cerebrum.data.repositories

import android.util.Log
import com.cerebrum.data.GetPhonebookResponse
import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.PhonebookContactRequest
import com.cerebrum.data.objectbox.BoxContacts
import com.cerebrum.data.objectbox.ObjectBox
import com.squareup.moshi.Json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhonebookRepository(
  private val apiClient: IWebClient,
  private val authStore: IAuthStore,
  private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseRepository() {

  private val boxContacts = BoxContacts()

  suspend fun loadFromServer() {
    try {
      val cached = execute {
        apiClient.getPhonebook(authStore.getToken())
      }.getOrThrow()
      boxContacts.addContacts(cached)
    }
    catch (e : Throwable) {
      Log.d("APPERROR", e.message ?: "")
    }
  }

  fun getAll() = boxContacts.items

  fun get(id: Long) = boxContacts.items.find { it.id == id }

  suspend fun add(
    name: String,
    phone: String,
    speciality: String
  ) = execute {
    apiClient.addPhonebookContact(
      authStore.getToken(),
      PhonebookContactRequest(
        name, phone, speciality
      )
    )
  }.also {
    if (it.isSuccess) {
      it.getOrNull()?.let {response ->
        boxContacts.addContact(
          GetPhonebookResponse(
            id = response.id,
            name, phone, speciality
          )
        )
      }
    }
  }

  suspend fun delete(id : Int) = execute {
    apiClient.deletePhonebookContact(
      authStore.getToken(),
      id
    )
  }.also {
    if (it.isSuccess) {
      boxContacts.remove(id.toLong());
    }
  }
}