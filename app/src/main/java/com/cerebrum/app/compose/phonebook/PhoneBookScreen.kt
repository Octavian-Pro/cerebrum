package com.cerebrum.app.compose.phonebook

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.callNumber
import com.cerebrum.app.compose.share.NavigationButton
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorBlue
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.data.objectbox.entities.Contact
import kotlinx.coroutines.launch


enum class Position {
  SINGLE, FIRST, LAST, MIDDLE
}

@Composable
fun PhoneBookScreen(
  navController: NavController
) {
  val vm = viewModel<PhonebookViewModel>()

  val selected by vm.selected.collectAsStateWithLifecycle()
  val grouped by vm.grouped.collectAsStateWithLifecycle()
  var searchQuery by remember {
    mutableStateOf("")
  }

  LaunchedEffect(Unit) {
    vm.load()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp, 0.dp, 0.dp, 32.dp)
  ) {
    PhonebookToolbar(
      navController = navController,
      query = searchQuery,
      onQueryChange = {
        searchQuery = it
        vm.filter(it)
      },
      onShow = {

      },
      onHide = {
        searchQuery = ""
        vm.resetFilter()
      }
    )
    PhonebookList(
      grouped,
      onContactClicked = {
        vm.setSelected(it)
      }
    )
    selected?.let { contact ->
      BottomDialogContact(
        contact = contact,
        onClose = {
          vm.clearSelection()
        },
        onDelete = {
          vm.deleteContact(it)
        }
      )
    }

  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomDialogContact(
  contact: Contact,
  onClose: () -> Unit,
  onDelete: (contact: Contact) -> Unit
) {

  val sheetState = rememberModalBottomSheetState()
  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onClose,
    sheetState = sheetState
  ) {

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp, 16.dp)
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = contact.description(),
        textAlign = TextAlign.Center
      )
      SpacerHeight(height = 8.dp)

      TextButton(
        modifier = Modifier
          .fillMaxWidth(),
        onClick = {
          callNumber(contact.phone, context)
          onClose()
        }
      ) {
        Text(text = stringResource(R.string.call_s, contact.phone))
      }
      TextButton(
        modifier = Modifier
          .fillMaxWidth(),
        onClick = {
          onDelete(contact)
          onClose()
        }
      ) {
        Text(
          text = stringResource(id = R.string.delete),
          color = Color(0xFFDC1A1A)
        )
      }
      SpacerHeight(height = 32.dp)
    }

  }
}



@Composable
fun Letter(
  letter : String
) {
  Box(
    modifier = Modifier
      .size(52.dp)
      .clip(CircleShape)
      .background(color = colorGreenDark),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = letter,
      fontSize = 32.sp,
      color = Color.White
    )
  }
}



@Preview
@Composable
fun PreviewPhoneBookScreen() {
  CerebrumTheme {
    Surface {
      PhoneBookScreen(rememberNavController())
    }
  }
}