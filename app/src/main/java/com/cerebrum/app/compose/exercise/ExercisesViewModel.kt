package com.cerebrum.app.compose.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.data.ApiClient
import com.cerebrum.data.objectbox.entities.UserExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExercisesViewModel: ViewModel() {

  private val testRepository = CerebrumApp.module.testRepository

  private val _exercises = MutableStateFlow<List<UserExercise>>(listOf())
  val exercises: StateFlow<List<UserExercise>> = _exercises.asStateFlow()

  fun load() {
    _exercises.value = testRepository.userExercise.filter {
      it.langs.contains(CerebrumApp.module.language.value, ignoreCase = true)
    }
  }

  fun getFileUrl(
    fileName: String,
    fileExtension: String,
  ): String {
    return "${ApiClient.BASE_URL}/API/user_exercises/file/${CerebrumApp.module.language.value}/${fileName}.${fileExtension}"
  }

  fun download(exercise: UserExercise, saveFileName: String) {
    if (exercise.fileName == null || exercise.fileExtension == null)
      return
    exercise.fileName?.let { fileName ->
      exercise.fileExtension?.let { fileExtension ->
        when (fileExtension.lowercase()) {
          "pdf" -> {
            CerebrumApp.module.downloader.downloadPdf(
              getFileUrl(exercise.fileName!!, exercise.fileExtension!!),
              saveFileName,
              //stringResource(id = ) "Упражнение ${exercise.exercisesName}",
              CerebrumApp.module.token
            )
          }
//          "png" -> {
//            CerebrumApp.module.downloader.downloadImage(
//              getFileUrl(fileName, fileExtension),
//              fileExtension,
//              "Упражнение ${exercise.exercisesName}",
//              CerebrumApp.module.token
//            )
//          }
          else -> {}
        }
      }

    }

  }
}