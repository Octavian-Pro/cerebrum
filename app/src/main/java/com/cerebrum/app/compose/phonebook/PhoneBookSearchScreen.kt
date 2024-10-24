package com.cerebrum.app.compose.phonebook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.share.NavigationButton
import com.cerebrum.app.ui.theme.CerebrumTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneBookSearchScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    TopAppBar(
      title = {
        Text(text = stringResource(R.string.search))
      },
      navigationIcon = {
        NavigationButton(
          navController = navController
        )
      }
    )
  }
}

@Preview
@Composable
fun PreviewPhoneBookSearchScreen() {
  CerebrumTheme {
    Surface {
      PhoneBookSearchScreen(
        navController = rememberNavController()
      )
    }
  }
}