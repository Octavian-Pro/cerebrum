package com.cerebrum.app.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.QUESTION_EMAIL
import com.cerebrum.app.R
import com.cerebrum.app.compose.ClickableEmailText
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme

@Composable
fun LinkSentToEmailScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = "",
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Image(
      painter = painterResource(id = R.drawable.email_sent_girl),
      contentDescription = null
    )
    Spacer(modifier = Modifier.height(40.dp))
    Surface(
      shadowElevation = 8.dp
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .paint(
            painter = painterResource(id = R.drawable.green_gradient),
            contentScale = ContentScale.FillBounds
          ),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = stringResource(R.string.confirm_link_sent),
          textAlign = TextAlign.Center,
          style = TextStyle(fontWeight = FontWeight.Bold),
          modifier = Modifier.padding(40.dp, 0.dp),
          fontSize = 18.sp
        )
      }
    }

    Column(
      modifier = Modifier
        .padding(0.dp, 0.dp, 16.dp, 16.dp)
        .weight(1f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Bottom
    ) {
      Text(text = stringResource(R.string.any_questions_q))
      Text(text = stringResource(R.string.you_can_write_to_us_by_email))
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

@Preview
@Composable
fun PreviewLinkSentToEmailScreen() {
  val navController = rememberNavController()
  CerebrumTheme {
    Surface {
      LinkSentToEmailScreen(navController)
    }
  }
}