package com.cerebrum.app.compose.articles

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.settings.AppFontSizes
import com.cerebrum.app.ui.theme.fontSizeSmall


@Composable
fun ArticleScreen(
  navController: NavController
) {
  val vm = viewModel<ArticleViewModel>()
  var isLoading by remember {
    mutableStateOf(false)
  }

    Column(
      modifier = Modifier
        .background(Color.White)
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Toolbar(
        text = vm.caption,
        onBackClicked = {
          navController.navigateUp()
        }
      )
      Column(
        modifier = Modifier
          .background(Color.Transparent)
          .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        //val state = rememberWebViewState("http://46.147.242.102:8881/API/article/file/lang/chto_takoe_insult.htm")
        AndroidView(
          factory = {
            WebView(it).apply {
              layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
              )
              webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                  isLoading = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {

                }

                override fun onReceivedError(
                  view: WebView?,
                  request: WebResourceRequest?,
                  error: WebResourceError?
                ) {
                  isLoading = false
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                  isLoading = false
                }
              }.apply {
                alpha = 0.99f
                settings.javaScriptEnabled = false
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.textZoom = when(CerebrumApp.module.preference.getAppFontSize()) {
                  AppFontSizes.SMALL -> 100
                  AppFontSizes.MEDIUM -> 150
                  AppFontSizes.LARGE -> 200
                }
                loadUrl(vm.url)
              }

            }
          }
        )
        if (isLoading) {
          CircularProgressIndicator()
        }

      }
    }
}