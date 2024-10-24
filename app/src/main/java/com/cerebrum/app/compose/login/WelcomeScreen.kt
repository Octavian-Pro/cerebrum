package com.cerebrum.app.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.QUESTION_EMAIL
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.ClickableEmailText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
  navController: NavController
) {
  
  LaunchedEffect(key1 = Unit) {
    if (CerebrumApp.module.hasCredentials) {
      navController.navigate("main")
    }
  }

  Column(
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .fillMaxWidth()
      .fillMaxHeight()
  ) {
    WelcomeToolbar()
    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .fillMaxHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Image(
        painter = painterResource(id = R.drawable.welcome_doctors),
        contentDescription = ""
      )

      Column(
        modifier = Modifier
          .width(188.dp)
          .padding(0.dp, 56.dp, 0.dp, 56.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp)
      ) {
        AppButton(
          caption = stringResource(R.string.registration),
          onClick = {
            navController.navigate("registration")
          },
          width = 188
        )
        AppButton(
          caption =  stringResource(id = R.string.login_enter),
          onClick = {
            navController.navigate("login")
          }
        )
      } // buttons column
      Column(
        modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
      ) {
        Text(text = stringResource(id = R.string.any_questions_q))
        Text(text = stringResource(id = R.string.you_can_write_to_us_by_email))
        Box(
          modifier = Modifier.padding(16.dp)
        ) {
          ClickableEmailText(
            email = QUESTION_EMAIL
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeToolbar() {
  Box(
    modifier = Modifier
      .background(Color.Transparent)
      .padding(0.dp, 0.dp, 0.dp, 16.dp)
      .fillMaxWidth()
  ) {
    Image(
      modifier = Modifier.fillMaxWidth(),
      painter = painterResource(id = R.drawable.background_topappbar),
      contentScale = ContentScale.FillBounds,
      contentDescription = ""
    )
    MediumTopAppBar(
      colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = Color.Transparent
      ),
      title = {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = stringResource(R.string.welcome),
          textAlign = TextAlign.Center,
        )
      }
    )
  }
}

@Preview
@Composable
fun WelcomeScreePreview() {
  val navController = rememberNavController()
  WelcomeScreen(navController)
}