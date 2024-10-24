package com.cerebrum.app.compose.settings

import android.widget.Space
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.login.WelcomeToolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorBlue
import com.cerebrum.app.ui.theme.colorLightGreen
import com.cerebrum.app.ui.theme.green
import kotlinx.coroutines.launch

// не локализовано! пока не используется
@Composable
fun SettingsScreen(
  navController: NavController,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
  ) {
    SettingsToolbar()
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Здравствуйте, <имя пользователя>!",
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "user_mail@mail.com",
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.W200
      )
      Spacer(modifier = Modifier.height(64.dp))
      SettingsButton(
        text = "Информация об аккаунте",
        iconId = R.drawable.ic_settings_person,
        onClick = {
          navController.navigate("account_info")
        }
      )
      SettingsButton(
        text = "Уведомления",
        iconId = R.drawable.ic_settings_bell,
        onClick = {
          navController.navigate("notifications")
        }
      )
      SettingsButton(
        text = "Тема",
        iconId = R.drawable.ic_settings_eye,
        onClick = {}
      )
      SettingsButton(
        text = "Безопасноть",
        iconId = R.drawable.ic_settings_security,
        onClick = {
          navController.navigate("settings-security-code")
        }
      )
      SettingsButton(
        text = "Язык",
        iconId = R.drawable.ic_settings_language,
        onClick = {}
      )
      SettingsButton(
        text = "О нас",
        iconId = R.drawable.ic_settings_person,
        onClick = {
          navController.navigate("about_us")
        }
      )
    }
  }
}

@Composable
fun SettingsButton(
  text : String,
  @DrawableRes iconId : Int,
  onClick : () -> Unit
) {
  TextButton(
    onClick,
    colors = ButtonDefaults.textButtonColors(
      contentColor = colorLightGreen
    ),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row {
        Icon(
          painter = painterResource(id = iconId),
          contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
          text,
          fontSize = 16.sp
        )
      }
      Icon(
        painter = painterResource(id = R.drawable.ic_arrow_right),
        contentDescription = null
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsToolbar() {
  val scope = rememberCoroutineScope()
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
          text = "Настройки",
          textAlign = TextAlign.Center,
        )
      },
      navigationIcon = {
        IconButton(onClick = {
          scope.launch {
//            drawerState.apply {
//              if (isClosed) open() else close()
//            }
          }
        }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "Открыть боковое меню",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    )
  }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
  CerebrumTheme {
    Surface {
      SettingsScreen(
        rememberNavController()
      )
    }
  }
}
