package com.cerebrum.app.compose.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.shadowBoxModifier
import com.cerebrum.app.ui.theme.CerebrumTheme

@Composable
fun AboutUsScreen(
  navController : NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp, 0.dp, 0.dp, 32.dp)
  ) {
    Toolbar(
      text = stringResource(R.string.about_us),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp, 80.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = stringResource(R.string.supported_by),
          fontSize = 16.sp,
          textAlign = TextAlign.Center
        )
        Image(
          painter = painterResource(
            id = R.drawable.fund_innovation
          ),
          contentDescription = null
        )
      }
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = 8.dp,
            RoundedCornerShape(8.dp)
          )
          .border(1.dp, Color(0xFFeaeeeb))
          .background(Color.White)
          .padding(16.dp)
      ) {
        Text(
          text = stringResource(R.string.sources_),
          fontWeight = FontWeight.W500
        )
      }
    }
  }
}

@Preview
@Composable
fun PreviewAboutUsScreen() {
  CerebrumTheme {
    Surface {
      AboutUsScreen(navController = rememberNavController())
    }
  }
}