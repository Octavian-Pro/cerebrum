package com.cerebrum.app.compose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreen100
import com.cerebrum.app.ui.theme.colorNotificationCyan
import com.cerebrum.app.ui.theme.colorNotificationCyanLight
import com.cerebrum.app.ui.theme.colorNotificationGreen
import com.cerebrum.app.ui.theme.colorNotificationGreenDark

@Composable
fun NotificationsScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(id = R.string.notification),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .clipToBounds(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(20) { idx ->
        NotificationItem(
          title = "Notification Title",
          text = "Lorem ipsum dolor sit amet, consectetur Lorem ipsum dolor sit amet, ipsum dolor",
          dotColor = when {
            idx % 2 == 0 -> colorNotificationGreen
            idx % 3 == 0 -> colorNotificationCyan
            idx % 5 == 0 -> colorNotificationCyanLight
            else -> colorNotificationGreenDark
          },
          onClick = {

          }
        )
      }
    }
  }
}

@Composable
fun NotificationItem(
  title : String,
  text : String,
  dotColor : Color,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
        onClick = {
          onClick()
        }
      )
      .shadow(
        elevation = 8.dp,
        RoundedCornerShape(8.dp)
      )
      .border(1.dp, Color(0xFFeaeeeb))
      .background(Color.White)
      .padding(16.dp)
  ) {
    Row(
      verticalAlignment = if (text.isEmpty()) {
        Alignment.CenterVertically
      } else {
        Alignment.Top
      }
    ) {
      Box(
        modifier = Modifier.padding(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(dotColor)
        )
      }
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = title,
          color = Color(0xFF1B512D)
        )
        if (text.isNotEmpty()) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text,
            color = Color(0xFF14AE5C)
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewNotificationItem() {
  NotificationItem(
    title = "Article title",
    text = "Lorem ipsum dolor sit amet, consectetur Lorem ipsum dolor sit amet, ipsum dolor",
    dotColor = colorNotificationGreen,
    onClick = {}
  )
}

@Preview
@Composable
fun PreviewNotificationsScreen() {
  CerebrumTheme {
    Surface {
      NotificationsScreen(rememberNavController())
    }
  }
}