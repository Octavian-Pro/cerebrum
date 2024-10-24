package com.cerebrum.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.compose.settings.AppFontSizes

val fontSizeSmall = 16.sp
val fontSizeMedium = 20.sp
val fontSizeLarge = 24.sp

val calendarDayFontSizeSmall = 18.sp
val calendarDayFontSizeMedium = 22.sp
val calendarDayFontSizeLarge = 26.sp

val calendarMonthFontSizeSmall = 12.sp
val calendarMonthFontSizeMedium = 26.sp
val calendarMonthFontSizeLarge = 28.sp

val calendarTitleFontSizeSmall = 22.sp
val calendarTitleFontSizeMedium = 26.sp
val calendarTitleFontSizeLarge = 30.sp

fun getCalendarDayFontSize() = when(CerebrumApp.module.preference.getAppFontSize()) {
  AppFontSizes.LARGE -> calendarDayFontSizeLarge
  AppFontSizes.MEDIUM -> calendarDayFontSizeMedium
  AppFontSizes.SMALL -> calendarDayFontSizeSmall
}

fun getCalendarMonthFontSize() = when(CerebrumApp.module.preference.getAppFontSize()) {
  AppFontSizes.LARGE -> calendarMonthFontSizeLarge
  AppFontSizes.MEDIUM -> calendarMonthFontSizeMedium
  AppFontSizes.SMALL -> calendarMonthFontSizeSmall
}

fun getCalendarTitleFontSize() = when(CerebrumApp.module.preference.getAppFontSize()) {
  AppFontSizes.LARGE -> calendarTitleFontSizeLarge
  AppFontSizes.MEDIUM -> calendarTitleFontSizeMedium
  AppFontSizes.SMALL -> calendarTitleFontSizeSmall
}

fun getFontSize() = when(CerebrumApp.module.preference.getAppFontSize()) {
  AppFontSizes.LARGE -> fontSizeLarge
  AppFontSizes.MEDIUM -> fontSizeMedium
  AppFontSizes.SMALL -> fontSizeSmall
}
// Set of Material typography styles to start with
val TypographySmall = Typography(
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = fontSizeSmall,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  )
  /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val TypographyMedium = Typography(
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = fontSizeMedium,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  )
)

val TypographyLarge = Typography(
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = fontSizeLarge,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  )
)