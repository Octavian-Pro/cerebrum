package com.cerebrum.app.compose.test

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark

@Composable
fun TestScreen(
  navController: NavController
) {
  val context = LocalContext.current
  val vm = viewModel<TestViewModel>()
  val isCompleted by vm.isTestCompleted.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    vm.loadAnamnesisTest()
  }

  LaunchedEffect(key1 = isCompleted) {
    if (isCompleted) {
      Toast.makeText(
        context,
        R.string.test_complete_go_to_exercises,
        Toast.LENGTH_LONG
      ).show()
      navController.navigateUp()
      navController.navigate("exercises")
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp, 0.dp, 0.dp, 32.dp)
  ) {
    Toolbar(
      text = stringResource(id = R.string.test),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    if (vm.isLoading.value) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
    }
    Column(
      modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      val items = vm.testItems
      for(item in items) {
        ShowTestItem(
          item,
          onAnswerClicked = { id, value ->
            vm.setAnswer(id, value)
          })
      }
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        if (vm.isAllAnswered.value) {
          AnimatedVisibility(
            visible = vm.isLoading.value,
            modifier = Modifier.padding(16.dp)
          ) {
            CircularProgressIndicator()
          }
          AppButton(
            enabled = vm.isLoading.value.not(),
            caption = stringResource(R.string.finish),
            color = colorGreenDark,
            onClick = {
              vm.sendResult()
            }
          )
        } else {
          Text(
            text = stringResource(R.string.need_complete_to_send_results)
          )
        }
      }
    }
  }
}

@Composable
fun ShowTestItem(
  item: AnamnesisItem,
  onAnswerClicked: (testId: Int, answer: Boolean) -> Unit
) {
  val buttonWidth = 120
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Text(
      text = item.question,
      color = colorGreenDark
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row {
      if (item.answer == null || item.answer == false) {
        AppButton(
          caption = stringResource(R.string.no),
          width = buttonWidth,
          height = 44,
          color = Color(0xFFB80E0E),
          //enabled = item.answer != null,
          onClick = {
            onAnswerClicked(item.id, false)
          })
      } else {
        Spacer(modifier = Modifier
          .width(buttonWidth.dp)
          .height(44.dp))
      }
      Spacer(modifier = Modifier.width(24.dp))
      if (item.answer == null || item.answer == true) {
        AppButton(
          caption = stringResource(R.string.yes),
          width = buttonWidth,
          height = 44,
          color = Color(0xFF0EB83E),
          //enabled = item.answer != null,
          onClick = {
            onAnswerClicked(item.id, true)
          })
      } else {
        Spacer(modifier = Modifier
          .width(buttonWidth.dp)
          .height(44.dp))
      }
    }
    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Preview
@Composable
fun PreviewShowTestItem() {
  CerebrumTheme {
    Surface {
      ShowTestItem(
        item = AnamnesisItem(id = 1, question = "Why?", answer = null),
        onAnswerClicked = { testId, answer ->

        }
      )
    }
  }
}

@Preview
@Composable
fun PreviewTestScreen() {
  CerebrumTheme {
    Surface {
      TestScreen(navController = rememberNavController())
    }
  }
}