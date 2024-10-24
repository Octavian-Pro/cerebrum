package com.cerebrum.app.compose.share

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cerebrum.app.R
import com.cerebrum.app.ui.theme.colorBlue
import com.cerebrum.app.ui.theme.colorGreenDark
import kotlinx.coroutines.launch

@Composable
fun NavigationButton(
  navController: NavController,
  drawerState: DrawerState? = null,
  isRoot : Boolean = false,
) {
  val scope = rememberCoroutineScope()

  IconButton(onClick = {
    if (isRoot) {
      scope.launch {
        drawerState?.apply {
          if (isClosed) open() else close()
        }
      }
    } else {
      navController.navigateUp()
    }
  }) {
    Icon(
      painter = if (isRoot)
        painterResource(id = R.drawable.ic_menu)
      else
        painterResource(id = R.drawable.ic_toolbar_back),
      contentDescription = stringResource(id = R.string.back),
      tint = if (isRoot) colorBlue else colorGreenDark
    )
  }
}