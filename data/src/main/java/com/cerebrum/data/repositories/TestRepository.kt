package com.cerebrum.data.repositories

import com.cerebrum.data.AnamnesisAnswerRequest
import com.cerebrum.data.AnamnesisItemResponse
import com.cerebrum.data.DiagnosticAnswerItem
import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.UserUpdateExerciseResponse
import com.cerebrum.data.objectbox.BoxUserExercise
import com.cerebrum.data.objectbox.entities.UserExercise

class TestRepository(
  private val apiClient: IWebClient,
  private val authStore: IAuthStore,
) : BaseRepository() {

  private val boxUserExercise = BoxUserExercise()

  suspend fun loadAnamnesisTest() : Result<Array<AnamnesisItemResponse>> {
    return execute {
      apiClient.getAnamnesisTest(
        authStore.getToken(),
        authStore.getLang().value
      )
    }
  }

  suspend fun updateAnamnesisTest(
    answers: List<AnamnesisAnswerRequest>
  ): Result<List<UserUpdateExerciseResponse>> {
    return execute {
      apiClient.updateUserExercisesByTest(
        authStore.getToken(), answers
      )
    }
  }

  fun saveUserExercises(value: List<UserUpdateExerciseResponse>) {
    boxUserExercise.removeAll()
    boxUserExercise.addAll(value)
  }

  val userExercise: List<UserExercise>
    get() = boxUserExercise.items

  fun get(id: Long) = boxUserExercise.get(id)


  suspend fun loadDiagnosticTest() = execute {
    apiClient.getDiagnosticTests(
      authStore.getToken(),
      authStore.getLang().value
    )
  }

  suspend fun sendDiagnosticTestAnswers(
    answers: List<DiagnosticAnswerItem>
  ) = execute {
    apiClient.sendDiagnosticTestAnswers(
      authStore.getToken(),
      answers
    )
  }
  suspend fun makeDiagnosticFile() = execute {
    apiClient.makeFile(authStore.getToken())
  }

}