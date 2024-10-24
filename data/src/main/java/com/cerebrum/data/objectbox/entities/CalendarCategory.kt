package com.cerebrum.data.objectbox.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CalendarCategory(
  @Id(assignable = true)
  var id: Long,
  var category: String = "", // “exercise”“drug”“event”
  val caption: String = "",
)