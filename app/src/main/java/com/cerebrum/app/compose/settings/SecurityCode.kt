package com.cerebrum.app.compose.settings

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGrayLight
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.colorGreenLightest
import com.cerebrum.app.ui.theme.colorLightGreen

// не локализовано!
@Composable
fun SecurityCode(
  navController: NavController
) {

  val isCodeSet = remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(R.string.security_code),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_lock),
        contentDescription = null,
        tint = if (isCodeSet.value) colorGreenDark else Color.Unspecified
      )
      Spacer(modifier = Modifier.height(24.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = 8.dp,
            RoundedCornerShape(8.dp)
          )
          .border(1.dp, Color(0xFFeaeeeb))
          .background(Color.White)
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (isCodeSet.value)
            "Код доступа активен"
          else
            "<Название приложения> будет запрашивать код доступа каждый раз, когда кто-то попытается открыть приложение",
          fontWeight = FontWeight.W400,
          textAlign = TextAlign.Center
        )
      }
      Spacer(modifier = Modifier.height(24.dp))
      Button(
        onClick = {
          //isCodeSet.value = !isCodeSet.value
          navController.navigate("set-security-code")
        },
        modifier = Modifier
          .width(250.dp)
          .height(46.dp)
          .shadow(
            elevation = 8.dp,
            RoundedCornerShape(8.dp)
          ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = colorGreenLightest,
          contentColor = Color.Black
        ),
      ) {
        Text(
          text = if (isCodeSet.value)
            "Отключить код доступа"
          else
            "Включить код доступа"
        )
      }

    }
  }
}

@Preview
@Composable
fun PreviewSecurityCode() {
  CerebrumTheme {
    SecurityCode(rememberNavController())
  }
}