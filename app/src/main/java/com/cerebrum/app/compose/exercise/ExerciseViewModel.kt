package com.cerebrum.app.compose.exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.ApiClient

class ExerciseViewModel(
  state: SavedStateHandle
): ViewModel() {

  private val id = checkNotNull(state.get<Long>("id"))

  var name by mutableStateOf("")
  var glideUrl by mutableStateOf<GlideUrl?>(null)
    private set
  var url by mutableStateOf("")
    private set
  var link by mutableStateOf("")

  init {
    val exercise = CerebrumApp.module.testRepository.get(id) ?: throw Exception("Unknown exercise id")
    val fileName = "${exercise.fileName}.${exercise.fileExtension}"
    name = exercise.exercisesName!!
    url = getFileUrl(fileName)
    glideUrl = createUrl(url)
    link = exercise.videoLink ?: ""
  }

  fun getFileUrl(
    fileName: String
  ): String {
    return "${ApiClient.BASE_URL}/API/user_exercises/file/${CerebrumApp.module.language.value}/${fileName}"
  }

  fun createUrl(url: String) =
    GlideUrl(
      url,
      LazyHeaders.Builder()
        .addHeader("Auth-Token", CerebrumApp.module.token)
        .build()
    )
}