package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cerebrum.data.objectbox.entities.CalendarEvent

@Composable
fun EventList(
  modifier: Modifier,
  events : List<CalendarEvent>,
  onDeleteClick: (item: CalendarEvent) -> Unit
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(events) { event ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = 8.dp,
            RoundedCornerShape(8.dp)
          )
          .clip(RoundedCornerShape(4.dp))
          .background(Color(0xFF89EDEF))
          .fillMaxWidth()
          .height(112.dp)
          // .animateItemPlacement()
      ) {
        EventItem(
          item = event,
          onDeleteClick = onDeleteClick
        )
      }
    }
  }
}