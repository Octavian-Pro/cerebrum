package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CalendarNavigationIcon(
  icon: Painter,
  contentDescription: String,
  onClick: () -> Unit,
) = Box(
  modifier = Modifier
    .fillMaxHeight()
    .aspectRatio(1f)
    .clip(shape = CircleShape)
    .clickable(role = Role.Button, onClick = onClick),
) {
  Icon(
    modifier = Modifier
      .fillMaxSize()
      .padding(4.dp)
      .align(Alignment.Center),
    painter = icon,
    contentDescription = contentDescription,
  )
}