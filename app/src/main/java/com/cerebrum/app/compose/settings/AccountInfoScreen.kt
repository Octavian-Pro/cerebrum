package com.cerebrum.app.compose.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGray100
import com.cerebrum.app.ui.theme.colorGreen100
import com.cerebrum.app.ui.theme.colorGreen200

@Composable
fun AccountInfoScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(R.string.account_info),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      ) {
        Spacer(modifier = Modifier.height(64.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = "Email"
          )
          Text(
            text = "user_mail@mail.com",
            color = colorGray100,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
          )
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = stringResource(id = R.string.password)
          )
//          Text(
//            text = "Обновлен <n> дней назад",
//            color = colorGray100,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1f)
//          )
        }
      }
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)) {
          TextButton(
            onClick = {

            },
            colors = ButtonDefaults.textButtonColors(
              contentColor = colorGreen200
            )
          ) {
            Text(text = stringResource(R.string.change_account))
          }
        }
        Image(
          painter = painterResource(id = R.drawable.bottom_rounds),
          contentDescription = null
        )
      }
    }
  }
}

@Preview
@Composable
fun PreviewAccountInfoScreen() {
  CerebrumTheme {
    AccountInfoScreen(
      rememberNavController()
    )
  }
}