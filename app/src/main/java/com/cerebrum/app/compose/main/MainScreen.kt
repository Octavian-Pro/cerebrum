package com.cerebrum.app.compose.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.BuildConfig
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.QUESTION_EMAIL
import com.cerebrum.app.R
import com.cerebrum.app.compose.ClickableEmailText
import com.cerebrum.app.compose.NavItem
import com.cerebrum.app.compose.Routes
import com.cerebrum.app.compose.ShadowBox
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.shadowBoxModifier
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorBlue

@Composable
fun MainScreen(
  navController: NavHostController,
) {

  val drawerState = rememberDrawerState(
    initialValue = DrawerValue.Closed
  )

  LaunchedEffect(key1 = Unit) {
    CerebrumApp.module.loadEvents()
  }

  MainDrawer(
    navController,
    drawerState
  ) {

    Column(
      modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      Toolbar(
        text = stringResource(R.string.main),
        onBackClicked = {
          navController.navigateUp()
        },
        textColor = colorBlue,
        isRoot = true,
        drawerState = drawerState
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        MainRow(
          text = stringResource(R.string.introductory_articles),
          iconId = R.drawable.ic_articles,
          onClick = {
            navController.navigate("articles")
          }
        )
        MainRow(
          text = stringResource(R.string.test),
          iconId = R.drawable.ic_tests,
          onClick = {
            navController.navigate("test")
          }
        )
        MainRow(
          text = stringResource(R.string.diagnostic_test),
          iconId = R.drawable.ic_tests,
          onClick = {
            navController.navigate(Routes.DiagnosticTestScreen)
          }
        )
        MainRow(
          text = stringResource(R.string.calendar),
          iconId = R.drawable.ic_calendar,
          onClick = {
            navController.navigate("calendar")
          }
        )
        MainRow(
          text = stringResource(R.string.selection_of_exercises),
          iconId = R.drawable.ic_youtube,
          onClick = {
            navController.navigate("exercises")
          })
        MainRow(
          text = stringResource(R.string.phonebook),
          iconId = R.drawable.ic_phone,
          onClick = {
            navController.navigate("phonebook")
          })
        Spacer(modifier = Modifier.weight(1f))
        Text(
          modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 0.dp),
          text = stringResource(R.string.support_fond_text),
          textAlign = TextAlign.Center,
          lineHeight = 16.sp,
          fontSize = 12.sp,
          color = Color(0xFF666666)
        )
        Image(
          painter = painterResource(id = R.drawable.fond_logo),
          contentDescription = ""
        )
      }
    }
  }
}

@Composable
fun MainRow(
  text : String,
  @DrawableRes iconId: Int,
  onClick : () -> Unit
) {
  Box(modifier = shadowBoxModifier(24.dp, onClick)) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = text,
        color = colorBlue
      )
      Image(
        painter = painterResource(id = iconId),
        contentDescription = null
      )
    }
  }
}

@Composable
fun MainDrawer(
  navController: NavController,
  drawerState: DrawerState,
  content: @Composable () -> Unit
) {

  val currentLogin = remember {
    mutableStateOf(CerebrumApp.module.currentLogin)
  }

  val gradient = Brush.verticalGradient(
    listOf(
      Color(0xFFA5FECB),
      Color(0xFF20BDFF),
      Color(0xFF5433FF),
    )
  )

  ModalNavigationDrawer(
    modifier = Modifier
      .background(Color.White),
    drawerState = drawerState,
    drawerContent = {

      ModalDrawerSheet(
        drawerContainerColor = Color.Transparent,
        drawerContentColor = Color.Transparent
      ) {
        Column(
          modifier = Modifier
            .background(gradient)
            .fillMaxSize(),
          verticalArrangement = Arrangement.SpaceBetween
        ) {

          Column(
            modifier = Modifier
              .fillMaxWidth()
          ) {
            SpacerHeight(height = 64.dp)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Text(
                text = currentLogin.value,
                color = Color.Black,
                modifier = Modifier.weight(1f)
              )
            }
            SpacerHeight(height = 64.dp)
            NavItem(
              drawerState,
              text = stringResource(id = R.string.change_language),
              iconId = R.drawable.ic_nav_language,
              onClick = {
                navController.navigate("change_language")
              }
            )
            NavItem(
              drawerState,
              text = stringResource(R.string.change_font_size),
              iconId = R.drawable.ic_nav_font_size,
              onClick = {
                navController.navigate("change_font_size")
              }
            )
            NavItem(
              drawerState,
              text = stringResource(R.string.logout),
              iconId = R.drawable.ic_nav_logout,
              onClick = {
                CerebrumApp.module.logout()
                navController.navigate("welcome") {
                  popUpTo(0)
                }
              }
            )
          }
          Column(
            modifier = Modifier
              .fillMaxWidth()
          ) {
            Text(
              text = stringResource(id = R.string.any_questions_q),
              color = Color.White,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth(),
            )
            Text(
              text = stringResource(id = R.string.you_can_write_to_us_by_email),
              color = Color.White,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth(),
            )
            ClickableEmailText(
              email = QUESTION_EMAIL,
              color = Color.White,
              textStyle = TextStyle(textAlign = TextAlign.Center),
              modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            )
            SpacerHeight(height = 64.dp)
            Text(
              text = "App Version 0.0.2",
              color = Color.White,
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      }
    }
  ) {
    content()
  }
}


@Preview
@Composable
fun PreviewMainScreen() {
  val navController = rememberNavController()
  CerebrumTheme {
    Surface {
      MainScreen(navController)
    }
  }
}