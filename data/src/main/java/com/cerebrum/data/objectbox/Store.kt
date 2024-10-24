package com.cerebrum.data.objectbox

import android.content.Context
import com.cerebrum.data.GetPhonebookResponse
import com.cerebrum.data.objectbox.entities.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
  lateinit var store: BoxStore
    private set

  fun init(context: Context) {
    store = MyObjectBox.builder()
      .androidContext(context.applicationContext)
      .build()
  }

}