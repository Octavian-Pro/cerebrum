package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cerebrum.app.R
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.data.ReminderTypes
import com.cerebrum.data.objectbox.entities.CalendarEvent

@Composable
fun EventItem(
  item: CalendarEvent,
  // text : String,
  onDeleteClick: (e: CalendarEvent) -> Unit
) {
  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp, 16.dp, 16.dp, 0.dp)
  ) {
    IconButton(
      modifier = Modifier
        .size(24.dp)
        .align(Alignment.TopEnd),
      onClick = {
        onDeleteClick(item)
      }) {
      Icon(
        painter = painterResource(id = R.drawable.ic_close),
        tint = colorGreenDark,
        contentDescription = null
      )
    }
    Column {
      Text(
        text = when (item.reminderType) {
          ReminderTypes.PILLS -> stringResource(id = R.string.taking_medication)
          ReminderTypes.DOCTOR_OR_EVENT -> item.description ?: stringResource(id = R.string.event_without_description)
          ReminderTypes.EXERCISE ->  stringResource(id = R.string.do_the_exercise)
          else -> "?"
        },
        fontWeight = FontWeight.W500
      )
      Text(
        text = item.printTime() // "10:00, 14:00, 18:00"
      )
    }
//    TextButton(
//      modifier = Modifier.align(Alignment.BottomEnd),
//      onClick = { /*TODO*/ }
//    ) {
//      Text(
//        text = "Изменить",
//        color = Color(0xFF00787A)
//      )
//    }
  }
}