package com.cerebrum.app.compose.phonebook

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cerebrum.app.compose.formatPhone
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.data.objectbox.entities.Contact


@Composable
fun PhoneItem(
  contact: Contact,
  position: Position,
  onClick: (contact: Contact) -> Unit
) {
  val shape = when (position) {
    Position.SINGLE -> RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
    Position.FIRST -> RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
    Position.LAST -> RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
    else -> RoundedCornerShape(0.dp)
  }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(shape = shape)
      .background(color = Color(0xFFE5E5E5))
      .clickable(
        onClick = {
          onClick(contact)
        }
      )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Letter(contact.name.firstOrNull()?.uppercase() ?: "")
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(16.dp, 0.dp, 0.dp, 0.dp)
      ) {
        Text(
          text = contact.description(),
          fontWeight = FontWeight.W500,
          color = colorGreenDark
        )
        Text(
          text = formatPhone(contact.phone),
          color = colorGreenDark
        )
      }
    }
  }
}