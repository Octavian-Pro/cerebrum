package com.cerebrum.app.compose.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.compose.SettingsOptionBox
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.fontSizeLarge
import com.cerebrum.app.ui.theme.fontSizeMedium
import com.cerebrum.app.ui.theme.fontSizeSmall
import kotlin.random.Random

enum class AppFontSizes(
  val value: Int
) {
  SMALL(0), MEDIUM(1), LARGE(2)
}

@Composable
fun ChangeFontSizeScreen(
  navController: NavController
) {

  val fontSize = remember {
    mutableStateOf(CerebrumApp.module.preference.getAppFontSize())
  }

  val confirmDialog = remember {
    mutableStateOf(false)
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = stringResource(R.string.font_size),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      SizeSettings(
        value = AppFontSizes.SMALL,
        state = fontSize.value,
        onClick = {
          onFontSizeChange(AppFontSizes.SMALL, fontSize, confirmDialog)
        }
      )
      SpacerHeight(height = 16.dp)
      SizeSettings(
        value = AppFontSizes.MEDIUM,
        state = fontSize.value,
        onClick = {
          onFontSizeChange(AppFontSizes.MEDIUM, fontSize, confirmDialog)
        }
      )
      SpacerHeight(height = 16.dp)
      SizeSettings(
        value = AppFontSizes.LARGE,
        state = fontSize.value,
        onClick = {
          onFontSizeChange(AppFontSizes.LARGE, fontSize, confirmDialog)
        }
      )
    }
    if (confirmDialog.value) {
      RestartConfirmDialog(confirmDialog)
    }
  }
}

fun onFontSizeChange(
  value: AppFontSizes,
  fontSizeState: MutableState<AppFontSizes>,
  confirmDialogState: MutableState<Boolean>
) {
  fontSizeState.value = value
  CerebrumApp.module.preference.setAppFontSize(value)
  confirmDialogState.value = true
}

fun restart(context: Context) {
  val packageManager: PackageManager = context.packageManager
  val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
  val componentName: ComponentName = intent.component!!
  val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
  context.startActivity(restartIntent)
  Runtime.getRuntime().exit(0)
}

@Composable
fun RestartConfirmDialog(
  confirmDialogState: MutableState<Boolean>
) {
  val context = LocalContext.current
  AlertDialog(
    title = {
      Text(text = stringResource(R.string.warning))
    },
    text = {
       Text(text = stringResource(R.string.need_restart_to_apply))
    },
    onDismissRequest = {
      confirmDialogState.value = false
    },
    confirmButton = {
      TextButton(onClick = { restart(context) }) {
        Text(text = stringResource(id = R.string.ok))
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          confirmDialogState.value = false
        }) {
        Text(text = stringResource(id = R.string.cancel))
      }
    }
  )
}


@Composable
fun SizeSettings(
  value: AppFontSizes,
  state : AppFontSizes,
  onClick : () -> Unit
) {
  SettingsOptionBox(onClick = {
    onClick()
  }) {
    Column(modifier = Modifier
      .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text = when (value) {
            AppFontSizes.SMALL -> {
              stringResource(R.string.font_size_normal)
            }

            AppFontSizes.MEDIUM -> {
              stringResource(R.string.font_size_large)
            }

            AppFontSizes.LARGE -> {
              stringResource(R.string.font_size_extra_lagre)
            }
          },
          style = TextStyle(
            fontSize = when(value) {
              AppFontSizes.SMALL -> {
                fontSizeSmall // 20.sp
              }
              AppFontSizes.MEDIUM -> {
                fontSizeMedium //30.sp
              }
              AppFontSizes.LARGE -> {
                fontSizeLarge //40.sp
              }
            }
          )
        )
        Switch(
          modifier = Modifier.align(Alignment.BottomEnd),
          checked = state == value,
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
                id = if (state == value)
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

@Preview
@Composable
fun PreviewChangeFontSizeScreen() {
  CerebrumTheme {
    Surface {
      ChangeFontSizeScreen(rememberNavController())
    }
  }
}

