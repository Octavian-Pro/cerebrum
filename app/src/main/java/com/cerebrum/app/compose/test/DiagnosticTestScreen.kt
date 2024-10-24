package com.cerebrum.app.compose.test

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.getFontSize
import com.cerebrum.data.ApiClient
import com.cerebrum.data.DiagnosticAnswerItem
import com.cerebrum.data.DiagnosisTestResponse
import com.cerebrum.data.DiagnosticMakeFileResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiagnosticTestScreen(
  navController: NavController
) {
  val vm = viewModel<DiagnosticTestViewModel>()
  val isCompleted by vm.isTestCompleted.collectAsStateWithLifecycle()
  val scrollState = rememberScrollState()

  LaunchedEffect(key1 = Unit) {
    vm.load()
  }

  LaunchedEffect(key1 = isCompleted) {
    if (isCompleted) {
      scrollState.scrollTo(0)
    }
  }

  val pullRefreshState = rememberPullRefreshState(
    refreshing = vm.updateStateIsLoading.value,
    onRefresh = { vm.loadState() }
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp, 0.dp, 0.dp, 32.dp)
  ) {
    Toolbar(
      text = stringResource(id = R.string.diagnostic_test),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Box(
      modifier = if (vm.hasPreviousResultFile.value?.first?.isReady != true)
        Modifier.pullRefresh(pullRefreshState)
      else
        Modifier
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        LastTestStatus(status = vm.hasPreviousResultFile.value)
        vm.testItems.value.forEach {
          DiagnosticTestItem(
            item = it,
            isCompleted = isCompleted,
            onAnswerChanged = { answers ->
              vm.updateAnswer(answers)
            }
          )
        }
        AnimatedVisibility(
          visible = vm.isLoading.value,
          modifier = Modifier.padding(16.dp)
        ) {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
        if (!vm.isLoading.value && !isCompleted) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.Center
          ) {
            if (vm.showSendButton.value) {
              AppButton(
                caption = "Отправить",
                width = 170,
                onClick = { vm.submit() }
              )
            } else {
              Text(
                text = stringResource(R.string.all_answers_needed)
              )
            }
          }
        }
      }
      PullRefreshIndicator(
        refreshing = vm.updateStateIsLoading.value,
        state = pullRefreshState,
        modifier = Modifier.align(Alignment.TopCenter)
      )
    }
  }
}

fun formatDate(calendar: Calendar): String {
  val fmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
  return fmt.format(calendar.time)
}

@Composable
fun LastTestStatus(
  status: Pair<DiagnosticMakeFileResponse?, Long>?
) {
  val calendar = Calendar.getInstance()
  status?.let {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp),
      horizontalArrangement = Arrangement.Center
    ) {
      calendar.timeInMillis = it.second

      it.first?.let { response ->
        if (response.isReady) {
          val saveAs = stringResource(id = R.string.file_name_diagnostic_test, response.fileName)
          TextButton(
            onClick = {
              onDownloadClicked(
                fileName = response.fileName,
                saveAs = saveAs
              )
            }
          ) {
            Text(
              text = stringResource(R.string.download_diagnostic_result_file, formatDate(calendar)),
              fontSize = getFontSize(),
              textAlign = TextAlign.Center
            )
          }
        } else {
          Text(
            fontSize = getFontSize(),
            text = stringResource(R.string.diagnostic_file_in_progress)
          )
        }
      }
    }
  }
}

fun onDownloadClicked(
  fileName: String,
  saveAs: String
) {
  val url = "${ApiClient.BASE_URL}/API/tests/diagnostic/answer/pull/${fileName}"
  CerebrumApp.module.downloader.downloadPdf(
    url,
    saveAs,
    CerebrumApp.module.token,
    withExt = true
  )
}

@Composable
fun TestItemView(
  id: Int,
  question: String,
  answers: List<String>?,
  onAnswerChanged: (DiagnosticAnswerItem) -> Unit,
  isCompleted: Boolean
) {
  val answer = remember {
    mutableStateOf("")
  }
  val subanswer = remember {
    mutableStateOf("")
  }
  val focusManager = LocalFocusManager.current
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Text(
      text = question,
      color = colorGreenDark
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row {
      answers?.let {
        ShowAnswers(
          answers = it,
          answered = answer.value,
          onClicked = { value ->
              focusManager.clearFocus()
              answer.value = value
              onAnswerChanged(DiagnosticAnswerItem(id = id, answer = value))
          }
        )
      }
      if (answers == null) {
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = subanswer.value,
            enabled = !isCompleted,
            isError = false,
            placeholder = {
              Text(stringResource(R.string.answer))
            },
            onValueChange = { value ->
              val childAnswer = DiagnosticAnswerItem(
                id = id,
                answer = value
              )
              subanswer.value = value
              onAnswerChanged(childAnswer)
            }
          )
        }
      }
    }
  }
}

@Composable
fun DiagnosticTestItem(
  item: DiagnosisTestResponse,
  onAnswerChanged: (List<DiagnosticAnswerItem>) -> Unit,
  isCompleted: Boolean
) {
  var activationAnswer by remember {
    mutableStateOf<DiagnosticAnswerItem?>(null)
  }
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    TestItemView(
      id = item.id,
      question = item.question,
      answers = item.answers,
      isCompleted = isCompleted,
      onAnswerChanged = { value ->
        activationAnswer = value
        activationAnswer?.let {
          onAnswerChanged(listOf(it))
        }
      }
    )
    AnimatedVisibility(visible = item.child?.activation != null && activationAnswer?.answer == item.child?.activation) {
      Column(
        modifier = Modifier.padding(
          start = 16.dp,
          top = 16.dp,
          end = 0.dp,
          bottom = 0.dp
        )
      ) {
        item.child?.subquestions?.forEach { child ->
          TestItemView(
            id = child.id,
            question = child.question,
            answers = child.answers,
            isCompleted = isCompleted,
            onAnswerChanged = { value ->
              activationAnswer?.let {
                onAnswerChanged(listOf(it, value))
              }
            }
          )
        }
      }
    }
    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Preview
@Composable
fun PreviewDiagnosticTestItem() {
  CerebrumTheme {
    Surface {
      DiagnosticTestItem(
        item = DiagnosisTestResponse(
          id = 0,
          number = 1,
          question = "Test test test",
          answers = listOf("Yes", "No"),
          child = null
        ),
        isCompleted = false,
        onAnswerChanged = {

        }
      )
    }
  }
}

@Preview
@Composable
fun PreviewDiagnosticTest() {
  CerebrumTheme {
    Surface {
      DiagnosticTestScreen(
        navController = rememberNavController()
      )
    }
  }
}