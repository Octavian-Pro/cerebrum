package com.cerebrum.app.compose.settings


import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.SettingsOptionBox
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme


enum class Languages(val value : String) {
  RU("ru"), EN("en")
}

@Composable
fun ChangeLanguageScreen(
  navController: NavController
) {

  var language by remember {
    mutableStateOf(Locale.current.language)
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = stringResource(R.string.change_language),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      SettingsOptionBox(onClick = {
        language = "ru"
        changeLanguage("ru")
      }) {
        Column(modifier = Modifier
          .fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_flag_ru),
            contentDescription = null
          )
          SpacerHeight(height = 32.dp)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Русский")
            Switch(

              checked = language == Languages.RU.value,
              onCheckedChange = null,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1AC8DC),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF0E71B8),
                uncheckedBorderColor = Color.Transparent,
                uncheckedIconColor = Color.Unspecified
              ),
              thumbContent = {
                Icon(
                  painter = painterResource(
                    id = if (language == "ru")
                      R.drawable.ic_switch_checked
                    else
                      R.drawable.ic_switch_unchecked
                  ),
                  contentDescription = null
                )
              }
            )
          }
        }
      }

      SpacerHeight(height = 16.dp)

      SettingsOptionBox(onClick = {
        language = "en"
        changeLanguage("en")
      }) {
        Column(modifier = Modifier
          .fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Image(
            painter = painterResource(id = R.drawable.flag_en),
            contentDescription = null
          )
          SpacerHeight(height = 32.dp)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "English")
            Switch(
              // modifier = Modifier.border(0.dp),
              checked = language == Languages.EN.value,
              onCheckedChange = null,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1AC8DC),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF0E71B8),
                uncheckedBorderColor = Color.Transparent,
                uncheckedIconColor = Color.Unspecified
              ),
              thumbContent = {
                Icon(
                  painter = painterResource(
                    id = if (language == Languages.EN.value)
                      R.drawable.ic_switch_checked
                    else
                      R.drawable.ic_switch_unchecked
                  ),
                  contentDescription = null
                )
              }
            )
          }
        }
      }
    }
  }
}

fun changeLanguage(language: String) {
  AppCompatDelegate.setApplicationLocales(
    LocaleListCompat.forLanguageTags(language)
  )
}

@Preview
@Composable
fun PreviewChangeLanguageScreen() {
  CerebrumTheme {
    Surface {
      ChangeLanguageScreen(navController = rememberNavController())
    }
  }
}