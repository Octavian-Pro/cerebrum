package com.cerebrum.app.compose.exercise

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.model.GlideUrl
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.openRutube
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark

@Composable
fun ExerciseScreen(
  navController : NavController
) {
  val vm = viewModel<ExerciseViewModel>()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFffffff))
      .padding(0.dp)
  ) {
    Toolbar(
      text = stringResource(R.string.exercise_s, vm.name),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ShowImageCoil(
        url = vm.url,
        link = vm.link
      )
    }
  }
}

@Composable
fun ShowImageCoil(
  url: String,
  link: String
) {
  val context = LocalContext.current

  val isShowProgress = remember {
    mutableStateOf(false)
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize(),
    contentPadding = PaddingValues(bottom=16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    // verticalArrangement = Arrangement.Center
  ) {
    item {
      Column(
        modifier = Modifier
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {

        if (isShowProgress.value) {
          CircularProgressIndicator()
        }
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(url)
            .addHeader("Auth-Token", CerebrumApp.module.token)
            .crossfade(true)
            .build(),
          modifier = Modifier
            .fillMaxSize(),
          contentScale = ContentScale.Crop,
          error = painterResource(id = R.drawable.ic_error),
          onLoading = {
            isShowProgress.value = true
          },
          onSuccess = {
            isShowProgress.value = false
          },
          onError = {
            isShowProgress.value = false
          },
          contentDescription = null
        )
        SpacerHeight(height = 32.dp)
        if (link.isNotEmpty()) {
          AppButton(
            caption = stringResource(R.string.video_explanation),
            width = 260,
            onClick = {
              context.openRutube(link)
            }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowImage(url: GlideUrl) {
  GlideImage(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    model = url,
    transition = CrossFade,
    // loading = placeholder(CircularProgressIndicator()).also { override() },
    failure = placeholder(R.drawable.ic_error),
    contentDescription = null
  )
}
@Composable
fun BottomPrevNextNav() {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    AppButton(
      onClick = { /*TODO*/ },
      caption = "Предыдущее",
      width = 158,
      height = 56,
      color = colorGreenDark
    )
    Spacer(modifier = Modifier.width(12.dp))
    AppButton(
      onClick = { /*TODO*/ },
      width = 158,
      height = 56,
      caption = "Следующее",
      color = colorGreenDark
    )
  }
}

@Preview
@Composable
fun PreviewExerciseScreen() {
  CerebrumTheme {
    Surface {
      ExerciseScreen(navController = rememberNavController())
    }
  }
}