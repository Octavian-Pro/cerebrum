package com.cerebrum.app.compose.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.colorGreenLightest

@Composable
fun SecurityScreen(
  navController : NavController
) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = stringResource(R.string.security),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp, 32.dp)
      ) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
          shape = RoundedCornerShape(8.dp),
          color = colorGreenLightest,
          shadowElevation = 12.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                navController.navigate("security_code")
              }
              .padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = stringResource(id = R.string.security_code))
          }
        }
      }
      Image(
        painter = painterResource(id = R.drawable.bottom_rounds),
        contentDescription = null
      )
    }
  }
}