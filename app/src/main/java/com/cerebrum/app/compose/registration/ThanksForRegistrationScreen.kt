package com.cerebrum.app.compose.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.ui.theme.CerebrumTheme

@Composable
fun ThanksForRegistrationScreen(
  navController : NavController
) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Image(
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth(),
      painter = painterResource(id = R.drawable.top_rounds),
      contentDescription = null)

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Image(
        painter = painterResource(id = R.drawable.thanks_girl),
        contentDescription = null
      )
      Text(text = stringResource(R.string.thank_you_for_registering))
      AppButton(
        caption = stringResource(R.string.forward_),
        onClick = {
          navController.navigate("main")
        }
      )
    }

    Image(
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth(),
      painter = painterResource(id = R.drawable.bottom_rounds),
      contentDescription = null)
  }
}

@Preview
@Composable
fun PreviewThanksForRegistrationScreen() {
  val navController = rememberNavController()
  CerebrumTheme {
    ThanksForRegistrationScreen(navController)
  }
}