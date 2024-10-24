package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreen200_50
import com.cerebrum.app.ui.theme.colorNotificationCyan
import com.cerebrum.app.ui.theme.colorNotificationGreen
import com.cerebrum.app.ui.theme.colorNotificationGreenDark
import com.cerebrum.app.ui.theme.getCalendarDayFontSize
import com.cerebrum.app.ui.theme.getCalendarMonthFontSize
import com.cerebrum.app.ui.theme.green
import com.cerebrum.data.ReminderTypes
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun Day(
  day: CalendarDay,
  isSelected: Boolean = false,
  hasPills: Boolean = false,
  hasDoctors: Boolean = false,
  hasExercises: Boolean = false,
  onClick: (CalendarDay) -> Unit = {},
) {
  val modifier = Modifier
    .aspectRatio(1f)
//    .border(
//      BorderStroke(1.dp, MaterialTheme.colorScheme.green)
//    )
    .background(
      color = if (isSelected)
        MaterialTheme.colorScheme.green
      else
        if (LocalDate.now() == day.date) {
          colorGreen200_50
        } else {
          Color.Transparent
        },
      shape = RoundedCornerShape(4.dp)
    )
    .clickable(
      enabled = day.position == DayPosition.MonthDate,
      onClick = { onClick(day) },
    )

  Box(
    modifier = modifier
  ) {
    val textColor = when (day.position) {
      DayPosition.MonthDate -> if (isSelected) Color.White else Color.Unspecified
      DayPosition.InDate, DayPosition.OutDate -> Color(0x33000000)
    }
    Text(
      modifier = Modifier
        .align(Alignment.TopCenter),
      text = day.date.dayOfMonth.toString(),
      color = textColor,
      fontSize = getCalendarDayFontSize() //18.sp,
    )
    if (day.position == DayPosition.MonthDate) {
      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (hasPills) EventLine(ReminderTypes.PILLS)
        if (hasDoctors) EventLine(ReminderTypes.DOCTOR_OR_EVENT)
        if (hasExercises) EventLine(ReminderTypes.EXERCISE)
      }
    }
  }
}

@Composable
fun EventLine(type: ReminderTypes) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(4.dp)
      .clip(RoundedCornerShape(4.dp))
      .alpha(0.3f)
      .background(
        when (type) {
          ReminderTypes.PILLS -> colorNotificationGreenDark
          ReminderTypes.DOCTOR_OR_EVENT -> colorNotificationGreen
          ReminderTypes.EXERCISE -> colorNotificationCyan
        }
      )
  )
  SpacerHeight(height = 2.dp)
}
//val colorNotificationGreenDark = Color(0xFF007F16)
//val colorNotificationGreen = Color(0xFF7AC74F)
//val colorNotificationCyan = Color(0xFF00BFA6)
@Preview
@Composable
fun PreviewDay() {
  CerebrumTheme {
    Surface {
      Day(
        day = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
        isSelected = false,

      )
    }
  }
}