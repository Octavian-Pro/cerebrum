package com.cerebrum.app.compose.phonebook

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cerebrum.app.R
import com.cerebrum.app.ui.theme.colorGray100
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.data.objectbox.entities.Contact

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhonebookList(
  grouped : Map<Char, List<Contact>>,
  onContactClicked: (contact: Contact) -> Unit
) {

  if (grouped.isEmpty()) {
    Box(modifier = Modifier
      .fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = stringResource(R.string.empty_phonebook),
        color = colorGray100
      )
    }
    return
  }

  LazyColumn(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(16.dp)
  ) {
    grouped.forEach { (char, contacts) ->
      stickyHeader {
        Text(
          text = char.toString(),
          color = colorGreenDark,
          fontSize = 20.sp,
          modifier = Modifier.padding(8.dp)
        )
      }
      itemsIndexed(contacts) { idx, item ->
        PhoneItem(
          contact = item,
          onClick = {
            onContactClicked(it)
          },
          position = if (contacts.size == 1) {
            Position.SINGLE
          } else if (idx == 0) {
            Position.FIRST
          } else if (idx == contacts.size - 1) {
            Position.LAST
          } else {
            Position.MIDDLE
          }
        )
      }
    }
  }
}