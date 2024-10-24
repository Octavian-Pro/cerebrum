package com.cerebrum.app.compose.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.SpacerWidth
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark

@Composable
fun ExerciseTypesScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(id = R.string.exercises),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Text(
        text = stringResource(R.string.exercises_types),
        color = colorGreenDark,
        fontSize = 22.sp
      )
      SpacerHeight(height = 16.dp)
      Text(
        text = stringResource(R.string.exercise_description)
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ) {
          AppButton(
            caption = stringResource(R.string.cognitive),
            color = colorGreenDark,
            onClick = {
              navController.navigate("exercise/${ExerciseTypes.COGNITIVE.value}")
            }
          )
          SpacerWidth(width = 12.dp)
          AppButton(
            caption = stringResource(R.string.physical),
            color = colorGreenDark,
            onClick = {
              navController.navigate("exercise/${ExerciseTypes.PHYSICAL.value}")
            }
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewExerciseTypesScreen() {
  CerebrumTheme {
    Surface {
      ExerciseTypesScreen(navController = rememberNavController())
    }
  }
}