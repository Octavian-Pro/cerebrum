package com.cerebrum.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.TextUnit
import androidx.core.view.WindowCompat
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.compose.settings.AppFontSizes

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF1AC8DC),
  surface = Color.White,
  onPrimary = Color.Green,
  secondary = Color.Green,
  tertiary = Color.Green,
  background = Color(0xFFFFFFFF),
)

private val LightColorScheme = lightColorScheme(
  primary = Color(0xFF1AC8DC),
  surface = Color.White,
  onPrimary = Color.Green,
  secondary = Color.Green,
  tertiary = Color.Green,
  background = Color(0xFFFFFFFF),

  /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val ColorScheme.link : Color @Composable
  get() = if (isSystemInDarkTheme()) md_theme_dark_link else md_theme_light_link

val ColorScheme.green : Color @Composable
get() = if (isSystemInDarkTheme()) md_theme_dark_green else md_theme_light_green

@Composable
fun CerebrumTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = LightColorScheme/*when {
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }*/
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true //darkTheme
    }
  }
  val currentFontSize = CerebrumApp.module.preference.getAppFontSize()
  MaterialTheme(
    colorScheme = colorScheme,
    typography = if (currentFontSize == AppFontSizes.LARGE) {
      TypographyLarge
    } else if (currentFontSize == AppFontSizes.MEDIUM) {
      TypographyMedium
    } else {
      TypographySmall
    },
    content = content
  )
}