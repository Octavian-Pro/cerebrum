package com.cerebrum.data.objectbox.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class UserExercise(
  @Id
  var id: Long = 0,
  var exerciseId: Int? = null,
  var fileName: String? = null,
  var exercisesName: String? = null,
  var fileExtension: String? = null,
  var videoLink: String? = null,
  var langs: String // ru,eng
)