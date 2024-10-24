package com.cerebrum.data.objectbox

import com.cerebrum.data.CalendarResponse
import com.cerebrum.data.objectbox.entities.CalendarCategory
import com.cerebrum.data.objectbox.entities.CalendarEvent
import com.cerebrum.data.objectbox.entities.CalendarEvent_
import io.objectbox.Box

class BoxCalendar: BaseBoxFor<CalendarEvent>(
  CalendarEvent::class.java
) {
  private val boxCategory by lazy {
    ObjectBox.store.boxFor(CalendarCategory::class.java)
  }

  fun clear() {
    boxCategory.removeAll()
    box.removeAll()
  }

  fun saveCategories(value: List<CalendarCategory>) {
    // привязываемся к
    // 1 = Приём лекарства
    // 2 = Упражнения
    // 4 = Другое
    if (boxCategory.all.size == 0) {
      boxCategory.put(value)
      return
    }
    value.forEach {
      if (!boxCategory.contains(it.id)) {
        boxCategory.put(it)
      }
    }
  }

  fun getForMonth(
    year: Int,
    month: Int
  ): List<CalendarEvent> {
    val query = box.query(
      CalendarEvent_.year.equal(year)
        .and(CalendarEvent_.month.equal(month))
    ).build()
    val result = query.find()
    query.close()
    return result
  }
  fun getForDay(
    year: Int,
    month: Int,
    day: Int,
  ): List<CalendarEvent> {
    val query = box.query(
      CalendarEvent_.year.equal(year).and(CalendarEvent_.month.equal(month).and(CalendarEvent_.day.equal(day)))
    ).build()
    val result = query.find()
    query.close()
    return result
  }

  fun getByUid(uid: String): CalendarEvent? {
    val query = box.query(
      CalendarEvent_.uid.equal(uid)
    ).build()
    val result = query.findFirst()
    query.close()
    return result
  }
}