package com.cerebrum.app.compose

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.ui.theme.CerebrumTheme

// не локализовано! не используется
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationReminderScreen(
  navController : NavController
) {

  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  var showBottomSheet by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = "Напоминания о приёме лекарств",
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painterResource(id = R.drawable.man_with_pencil_and_calendar),
        contentDescription = null
      )
      Spacer(modifier = Modifier.height(32.dp))
      Text(
        text = "Давайте начнём!",
        style = TextStyle(
          color = Color.Black,
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        )
      )
      Text(
        text = "Получайте напоминания о лекарствах, мероприятих и многом другом.",
        style = TextStyle(
          color = MaterialTheme.colorScheme.primary,
          fontSize = 18.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
      )
      Spacer(modifier = Modifier.height(64.dp))
      AppButton(
        caption = "Добавить лекарство",
        width = 226,
        onClick = {
          showBottomSheet = true
        }
      )

      if (showBottomSheet) {
        ModalBottomSheet(
          onDismissRequest = { showBottomSheet = false },
          sheetState = sheetState,
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_double_down),
              contentDescription = null
            )
            TextButton(
              onClick = { /*TODO*/ },
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_btn_pills),
                  tint = Color.Black,
                  contentDescription = null
                )
                Text(
                  text = "Лекарство",
                  modifier = Modifier.fillMaxWidth(),
                  color = Color.Black,
                  textAlign = TextAlign.Center
                )
              }
            }
            TextButton(
              onClick = { /*TODO*/ },
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_btn_flag),
                  tint = Color.Black,
                  contentDescription = null
                )
                Text(
                  text = "Мероприятия",
                  modifier = Modifier.fillMaxWidth(),
                  color = Color.Black,
                  textAlign = TextAlign.Center)
              }
            }
            TextButton(
              onClick = { /*TODO*/ },
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_btn_happy),
                  tint = Color.Black,
                  contentDescription = null
                )
                Text(
                  text = "Контроль симптомов",
                  modifier = Modifier.fillMaxWidth(),
                  color = Color.Black,
                  textAlign = TextAlign.Center)
              }
            }
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewMedicationReminderScreen() {
  val navController = rememberNavController()
  CerebrumTheme {
    Box(
      modifier = Modifier.background(Color.White)
    ) {
      MedicationReminderScreen(navController)
    }
  }
}