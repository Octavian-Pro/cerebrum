package com.cerebrum.app.compose.articles

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.ApiClient

class ArticleViewModel(
  state: SavedStateHandle
): ViewModel() {

  private val _caption = checkNotNull(state.get<String>("name"))
  private val _fileName = checkNotNull(state.get<String>("fileName"))

  val caption = _caption
  val url = "${ApiClient.BASE_URL}/API/article/file/${CerebrumApp.module.language.value}/${_fileName}"

  init {
    Log.d("APPLOG", url)
  }
}