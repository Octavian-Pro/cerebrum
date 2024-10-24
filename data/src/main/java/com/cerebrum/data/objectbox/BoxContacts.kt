package com.cerebrum.data.objectbox

import com.cerebrum.data.GetPhonebookResponse
import com.cerebrum.data.objectbox.entities.Contact

class BoxContacts: BaseBoxFor<Contact>(Contact::class.java) {

  fun addContacts(items: Collection<GetPhonebookResponse>) {
    box.put(items.map {
        Contact(
          id = it.id.toLong(),
          name = it.name,
          phone = it.phone,
          speciality = it.speciality
        )
      })
  }

  fun addContact(item : GetPhonebookResponse) {
    box.put(
        Contact(
          id = item.id.toLong(),
          name = item.name,
          phone = item.phone,
          speciality = item.speciality
        )
      )
  }
}