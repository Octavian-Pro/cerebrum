package com.cerebrum.data.repositories

import android.util.Log
import com.cerebrum.data.CalendarAddRequest
import com.cerebrum.data.CalendarResponse
import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.ReminderTypes
import com.cerebrum.data.objectbox.BoxCalendar
import com.cerebrum.data.objectbox.entities.CalendarCategory
import com.cerebrum.data.objectbox.entities.CalendarEvent
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CalendarRepository(
  private val apiClient: IWebClient,
  private val authStore: IAuthStore
): BaseRepository() {

  private val serverFormatter by lazy {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale("ru", "RU")).apply {
      timeZone = TimeZone.getTimeZone("Europe/Moscow")
    }
  }

  val box = BoxCalendar()

  fun getForMonth(
    year: Int,
    month: Int
  ): List<CalendarEvent> = box.getForMonth(year, month)


  fun getForDay(
    year: Int,
    month: Int,
    day: Int
  ) = box.getForDay(year, month, day)
  suspend fun create(
    type: ReminderTypes,
    date: Calendar,
    description: String?
  ): CalendarEvent {
    val event = CalendarEvent(
      id = 0,
      year = date.get(Calendar.YEAR),
      month = date.get(Calendar.MONTH) + 1,
      day = date.get(Calendar.DAY_OF_MONTH),
      hour = date.get(Calendar.HOUR_OF_DAY),
      minute = date.get(Calendar.MINUTE),
      reminderType = type,
      description = description
    )
    val request = CalendarAddRequest(
      name = description,
      category = type.id,
      date = serverFormatter.format(date.time)
    )
    Log.d("CalendarTag", request.date)
    val response = apiClient.addCalendar(authStore.getToken(), request)
    if (response.isSuccessful) {
      response.body()?.let {
        event.uid = it.id
        box.add(event)
      }
    }
    return event
  }

  suspend fun remove(id: Long, uid: String) {
    box.remove(id)
    apiClient.deleteCalendar(authStore.getToken(), uid)
  }

  private fun serverCategoryToReminderType(
    value: String?
  ): ReminderTypes {
    // 1 = Приём лекарства
    // 2 = Упражнения
    // 4 = Другое
    if (value == "Приём лекарств") return ReminderTypes.PILLS
    if (value == "Упражнения") return ReminderTypes.EXERCISE
    return ReminderTypes.DOCTOR_OR_EVENT
  }
//  private fun serverCategoryIdFromName(
//    value: String?
//  ): Int {
//    if (value == "Приём лекарства") return 1
//    if (value == "Упражнения") return 2
//    return 4
//  }
//
//  private fun serverCategoryIdFromReminderType(
//    value: ReminderTypes
//  ): Int {
//    if (value == ReminderTypes.PILLS) return 1
//    if (value == ReminderTypes.EXERCISE) return 2
//    return 4
//  }
//

  fun fromServerToLocal(
    response: CalendarResponse
  ): CalendarEvent? {
    val calendar = Calendar.getInstance()
    calendar.time = serverFormatter.parse(response.date) ?: return null
    if (Calendar.getInstance().time.after(calendar.time)) return null
    // calendar
    return CalendarEvent(
      id = 0,
      uid = response.id,
      reminderType = serverCategoryToReminderType(response.category),
      year = calendar[Calendar.YEAR],
      month = calendar[Calendar.MONTH] + 1,
      day = calendar[Calendar.DAY_OF_MONTH],
      hour = calendar[Calendar.HOUR_OF_DAY],
      minute = calendar[Calendar.MINUTE],
      description = response.name
    )
  }

  fun clearLocalOnLogout() {
    box.clear()
  }
  suspend fun load(lang: String): List<CalendarEvent> {
    val result = mutableListOf<CalendarEvent>()
    apiClient.getCalendarCategories(authStore.getToken(), lang).let {
      if (it.isSuccessful) {
        it.body()?.let { response ->
          val categories = response.map { categoryResponse ->
            CalendarCategory(
              id = categoryResponse.id.toLong(),
              caption = categoryResponse.caption,
              category = categoryResponse.category
            )
          }
          if (categories.isNotEmpty()) {
            box.saveCategories(categories)
          }
        }
      }
    }
    val response = apiClient.getCalendar(authStore.getToken())
    if (response.isSuccessful) {
      response.body()?.let {
        it.forEach { eventResponse ->
          Log.d("CalendarTag", "SERVER Name: ${eventResponse.name}, Category: ${eventResponse.category} Date: ${eventResponse.date}")
          fromServerToLocal(eventResponse)?.let { event ->

            Log.d("CalendarTag", "LOCAL Name: ${event.description}, Category: ${event.reminderType} Date: ${event.year}-${event.month}-${event.day} ${event.hour}:${event.minute}:00")

            event.uid?.let { eventUid ->
              if (box.getByUid(eventUid) == null) {
                Log.d("CalendarTag", eventResponse.date)
                result.add(event)
                box.add(event)
              }
            }
          }
        }
      }
    }
    return result
  }
}