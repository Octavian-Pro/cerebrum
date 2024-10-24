package com.cerebrum.app.compose.phonebook

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.share.NavigationButton
import com.cerebrum.app.ui.theme.colorGray100
import com.cerebrum.app.ui.theme.colorGrayLightest
import com.cerebrum.app.ui.theme.colorGreenDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonebookToolbar(
  navController: NavController,
  query: String,
  onQueryChange: (String) -> Unit,
  onShow: () -> Unit,
  onHide: () -> Unit,
  isRoot : Boolean = false,
  drawerState: DrawerState? = null,
  textColor : Color? = null,
) {

  val focusRequester = FocusRequester()
  var showSearch by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = showSearch) {
    if (showSearch) {
      onShow()
      focusRequester.requestFocus()
    } else {
      onHide()
    }
  }

  AnimatedVisibility(
    visible = showSearch,
//    enter = fadeIn() + slideInHorizontally(),
//    exit = slideOutHorizontally() + fadeOut(),
//    enter: EnterTransition = fadeIn() + expandIn(),
//    exit: ExitTransition = shrinkOut() + fadeOut(),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 8.dp, 16.dp, 0.dp)
    ) {
      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(50))
          .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = colorGrayLightest,
          unfocusedContainerColor = colorGrayLightest,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
        ),
        value = query,
        onValueChange = {
          onQueryChange(it)
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null
          )
        },
        trailingIcon = {
          Icon(
            modifier = Modifier.clickable {
              showSearch = false
            },
            imageVector = Icons.Default.Close,
            contentDescription = null
          )
        }
      )
      /*SearchBar(
        modifier = Modifier
          .fillMaxWidth(),
        placeholder = {
          Text(text = "Поиск в телефонной книге...")
        },
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onQueryChange,
        active = false,
        onActiveChange = {

        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null
          )
        },
        trailingIcon = {
          Icon(
            modifier = Modifier.clickable {
              showSearch = false
            },
            imageVector = Icons.Default.Close,
            contentDescription = null
          )
        }
      ) {}*/
    }
  }

  AnimatedVisibility(
    visible = !showSearch,
  ) {
    TopAppBar(
      title = {
        Text(
          text = stringResource(id = R.string.phonebook),
          color = textColor ?: colorGreenDark
        )
      },
      navigationIcon = {
        NavigationButton(
          navController = navController,
          drawerState = drawerState,
          isRoot = isRoot
        )
      },
      actions = {
        IconButton(onClick = {
          navController.navigate("phonebook/add/0")
        }) {
          Icon(
            imageVector = Icons.Outlined.Add,
            tint = colorGreenDark,
            contentDescription = null
          )
        }
        IconButton(onClick = {
          showSearch = true
        }) {
          Icon(
            imageVector = Icons.Outlined.Search,
            tint = colorGreenDark,
            contentDescription = null
          )
        }
      }
    )
  }
}