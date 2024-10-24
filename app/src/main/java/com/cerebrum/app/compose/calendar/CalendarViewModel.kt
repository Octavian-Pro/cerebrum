package com.cerebrum.app.compose.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.ReminderTypes
import com.cerebrum.data.objectbox.entities.CalendarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

//data class Event(
//  val id: Long,
//  val date: LocalDate,
//  val hour: Int,
//  val minute: Int
//) {
//  override fun toString(): String {
//    return "$id $date $hour:$minute"
//  }
//}

class CalendarViewModel: ViewModel() {

  private val repository = CerebrumApp.module.calendarRepository

  private var _currentMonth: YearMonth = YearMonth.now()
  private var _currentDay: LocalDate? = null
  private var _monthEvents: List<CalendarEvent> = listOf()
  private val _events = MutableStateFlow<Map<Int, List<CalendarEvent>>>(mapOf())
  //val events: StateFlow<Map<Int, List<CalendarEvent>>> = _events.asStateFlow()

  private val _currentEvents = MutableStateFlow<List<CalendarEvent>>(listOf())
  val currentEvents: StateFlow<List<CalendarEvent>> = _currentEvents

  private val _newEvent = MutableStateFlow<CalendarEvent?>(null)
  val newEvent: StateFlow<CalendarEvent?> = _newEvent

  init {
    update()
  }

  fun setCurrentMonth(yearMonth: YearMonth) {
    _currentMonth = yearMonth
    update()
  }

  fun setCurrentDay(date: LocalDate) {
    _currentDay = date
    updateCurrentEvents()
  }

  private fun update() {
    updateMonth()
    updateCurrentEvents()
  }
  private fun updateMonth() {
    _monthEvents = repository
      .getForMonth(_currentMonth.year, _currentMonth.monthValue)
    _events.value = _monthEvents
      .groupBy { it.day }
  }
  private fun updateCurrentEvents() {
    _currentDay?.let {
      _currentEvents.value = _events.value[it.dayOfMonth] ?: listOf()
    }
  }

  fun clearSelection() {
    _events.value = mapOf()
  }

  fun removeReminder(
    id: Long,
    uid: String
  ) {
    viewModelScope.launch {
      repository.remove(id, uid)
      update()
    }
  }
  fun hasPills(date: LocalDate): Boolean {
    return _events.value[date.dayOfMonth]?.any { it.reminderType == ReminderTypes.PILLS } ?: false
  }

  fun hasDoctors(date: LocalDate): Boolean {
    return _events.value[date.dayOfMonth]?.any { it.reminderType == ReminderTypes.DOCTOR_OR_EVENT } ?: false
  }

  fun hasExercise(date: LocalDate): Boolean {
    return _events.value[date.dayOfMonth]?.any { it.reminderType == ReminderTypes.EXERCISE } ?: false
  }
  fun addReminder(
    type: ReminderTypes,
    date: LocalDate,
    hour: Int,
    minute: Int,
    description: String?
  ) {
    viewModelScope.launch {
      val calendar = Calendar.getInstance()
      calendar.set(Calendar.YEAR, date.year)
      calendar.set(Calendar.MONTH, date.monthValue - 1)
      calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
      calendar.set(Calendar.HOUR_OF_DAY, hour)
      calendar.set(Calendar.MINUTE, minute)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)
      val event = repository.create(type, calendar, description)
      update()
      _newEvent.value = event
    }
    // add to local db
    // add to api
    // create AlarmManager
  }
}
