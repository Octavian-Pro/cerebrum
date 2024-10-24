package com.cerebrum.data.objectbox.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Contact(
  @Id(assignable = true)
  var id: Long = 0,
  var name: String = "",
  var phone: String = "",
  var speciality: String = "",
) {
  fun description() = "$name${if (speciality.isEmpty()) "" else "($speciality)"}"
}