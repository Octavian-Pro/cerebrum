package com.cerebrum.app.compose.articles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.settings.NotificationItem
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorNotificationCyan
import com.cerebrum.app.ui.theme.colorNotificationCyanLight
import com.cerebrum.app.ui.theme.colorNotificationGreen
import com.cerebrum.app.ui.theme.colorNotificationGreenDark

@Composable
fun ArticlesListScreen(
  navController: NavController
) {

  val vm = viewModel<ArticlesViewModel>()
  val isLoading = vm.isLoading.collectAsStateWithLifecycle()
  val articles = vm.articles.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    vm.load()
  }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(R.string.introductory_articles_about_stroke),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    if (isLoading.value) {
      CircularProgressIndicator()
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .clipToBounds(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(
          items = articles.value,
          key = { article -> article.fileName }
        ) { article ->
          NotificationItem(
            onClick = {
              navController.navigate("article/${article.name}/${article.fileName}.${article.fileExtension}")
            },
            title = article.name,
            text = "",
            dotColor = colorNotificationGreen
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewArticlesListScreen() {
  CerebrumTheme {
    ArticlesListScreen(navController = rememberNavController())
  }
}