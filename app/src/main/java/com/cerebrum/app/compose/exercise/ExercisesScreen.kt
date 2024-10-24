package com.cerebrum.app.compose.exercise

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.Routes
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.openRutube
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.data.objectbox.entities.UserExercise

enum class ExerciseTypes(val value : String) {
  COGNITIVE("cognitive"),
  PHYSICAL("physical")
}

@Composable
fun ExercisesScreen(
  navController: NavController
) {
  val vm = viewModel<ExercisesViewModel>()
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    vm.load()
  }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(R.string.exercises),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    val exercises = vm.exercises.collectAsStateWithLifecycle()
    if (exercises.value.isEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(text = stringResource(R.string.you_must_take_a_test_to_select_exercises))
        SpacerHeight(height = 32.dp)
        AppButton(
          caption = stringResource(R.string.take_the_test),
          width = 210,
          onClick = {
            navController.navigate(Routes.TestScreen)
          }
        )
      }
    }
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .clipToBounds(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(exercises.value) {
        ExerciseItem(
          exercise = it,
          dotColor = colorGreenDark,
          onClick = { exercise, fileName ->
            onExerciseClicked(exercise, fileName, vm, navController)
          }
        )
      }
    }
  }
}

fun onExerciseClicked(
  exercise: UserExercise,
  saveFileName: String,
  vm: ExercisesViewModel,
  navController: NavController
) {
  when {
    exercise.isPng() -> {
      navController.navigate("exercise/${exercise.id}")
    }
    exercise.isPdf() -> {
      vm.download(exercise, saveFileName)
    }
  }
}

fun UserExercise.isPng(): Boolean {
  return fileExtension?.lowercase() == "png"
}

fun UserExercise.isPdf(): Boolean {
  return fileExtension?.lowercase() == "pdf"
}

fun UserExercise.hasVideo(): Boolean {
  return videoLink.isNullOrEmpty().not()
}

@Composable
fun ExerciseItem(
  exercise: UserExercise,
  dotColor : Color,
  onClick: (exercise: UserExercise, fileName: String) -> Unit
) {
  val fileName = stringResource(R.string.exercise_s, exercise.exercisesName ?: "0")
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
        onClick = {
          onClick(exercise, fileName)
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
    Row() {
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
          text = stringResource(R.string.exercise_s, exercise.exercisesName ?: "0"),
          color = Color(0xFF1B512D)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = showFileNameOrUrl(exercise),
            color = Color(0xFF14AE5C)
          )
          Row() {
            if (exercise.isPdf()) {
              Icon(
                painter = painterResource(id = R.drawable.ic_download),
                tint = colorGreenDark,
                contentDescription = null
              )
            }
            if (exercise.isPng()) {
              Icon(
                painter = painterResource(id = R.drawable.ic_image),
                tint = colorGreenDark,
                contentDescription = null
              )
            }
            if (exercise.hasVideo()) {
              Icon(
                painter = painterResource(id = R.drawable.ic_play),
                tint = colorGreenDark,
                contentDescription = null
              )
            }
          }
        }

      }
    }
  }
}

fun showFileNameOrUrl(exercise: UserExercise): String {
  // if (exercise.hasVideo()) return exercise.videoLink!!
  return "${exercise.fileName}.${exercise.fileExtension}"
}

@Preview
@Composable
fun PreviewExerciseItem() {
  ExerciseItem(
    exercise = UserExercise(
      id = 1,
      exerciseId = 112,
      fileName = "112",
      exercisesName = "112",
      fileExtension = "pdf",
      videoLink = null,
      langs = "ru,eng"
    ),
    dotColor = colorGreenDark,
    onClick = { _,_ ->
    }
  )
}

@Preview
@Composable
fun PreviewExercisesScreen() {
  CerebrumTheme {
    Surface {
      ExercisesScreen(navController = rememberNavController())
    }
  }
}