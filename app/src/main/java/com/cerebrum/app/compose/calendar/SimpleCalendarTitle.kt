package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.compose.settings.AppFontSizes
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.getCalendarTitleFontSize
import java.time.YearMonth


@Composable
fun SimpleCalendarTitle(
  modifier: Modifier,
  currentMonth: YearMonth,
  goToPrevious: () -> Unit,
  goToNext: () -> Unit,
) {

  val height = when (CerebrumApp.module.preference.getAppFontSize()) {
    AppFontSizes.SMALL -> 40.dp
    AppFontSizes.MEDIUM -> 44.dp
    AppFontSizes.LARGE -> 48.dp
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
      .background(Color.Transparent),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    CalendarNavigationIcon(
      icon = painterResource(id = R.drawable.ic_chevron_left),
      contentDescription = stringResource(R.string.back),
      onClick = goToPrevious,
    )
    Text(
      modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(colorGreenDark)
        .padding(8.dp)
        .testTag("MonthTitle"),
      text = currentMonth.displayText(),
      color = Color.White,
      fontSize = getCalendarTitleFontSize(), //22.sp,
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Medium,
    )
    CalendarNavigationIcon(
      icon = painterResource(id = R.drawable.ic_chevron_right),
      contentDescription = stringResource(R.string.forward),
      onClick = goToNext,
    )
  }
}