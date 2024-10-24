package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cerebrum.app.ui.theme.getCalendarMonthFontSize
import java.time.DayOfWeek

@Composable
fun MonthHeader(
  modifier: Modifier = Modifier,
  daysOfWeek: List<DayOfWeek> = emptyList(),
) {
  Row(
    modifier
      .fillMaxWidth()
      .height(28.dp)
  ) {
    for (dayOfWeek in daysOfWeek) {
      Text(
        modifier = Modifier.weight(1f),
        textAlign = TextAlign.Center,
        fontSize = getCalendarMonthFontSize(),//12.sp,
        text = dayOfWeek.displayText(uppercase = true),
        fontWeight = FontWeight.Light,
      )
    }
  }
}