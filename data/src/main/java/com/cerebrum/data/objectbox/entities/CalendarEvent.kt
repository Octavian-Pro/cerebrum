package com.cerebrum.data.objectbox.entities

import com.cerebrum.data.ReminderTypes
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Entity
data class CalendarEvent(
  @Id
  var id: Long,
  var uid: String? = null,
  @Convert(converter = ReminderTypeConvertor::class, dbType = String::class)
  var reminderType: ReminderTypes? = null,
  var year: Int,
  var month: Int,
  var day: Int,
  var hour: Int,
  var minute: Int,
  var description: String? = null
) {
  fun printTime(): String {
    return "${if (hour < 10) "0$hour" else hour}:${if (minute < 10) "0$minute" else minute}"
  }
}

class ReminderTypeConvertor: PropertyConverter<ReminderTypes?, String?> {
  override fun convertToDatabaseValue(entityProperty: ReminderTypes?): String? {
    return entityProperty?.id
  }

  override fun convertToEntityProperty(databaseValue: String?): ReminderTypes? {
    if (databaseValue == null) return null
    return ReminderTypes
      .values()
      .firstOrNull { it.id == databaseValue }
  }
}