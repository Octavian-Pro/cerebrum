package com.cerebrum.data.objectbox

import com.cerebrum.data.UserUpdateExerciseResponse
import com.cerebrum.data.objectbox.entities.UserExercise

class BoxUserExercise: BaseBoxFor<UserExercise>(
  UserExercise::class.java
) {

  fun addAll(values: List<UserUpdateExerciseResponse>) {
    box.put(values.map {
      UserExercise(
        exerciseId = it.exercisesId,
        fileName = it.fileName,
        exercisesName = it.exercisesName,
        fileExtension = it.fileExtension,
        videoLink = it.videoLink,
        langs = it.lang.joinToString(",")
      )
    })
  }

}