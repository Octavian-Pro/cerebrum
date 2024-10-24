package com.cerebrum.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cerebrum.app.compose.calendar.showNotification
import com.cerebrum.data.ReminderTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class AlarmReceiver: BroadcastReceiver() {

  @OptIn(DelicateCoroutinesApi::class)
  override fun onReceive(context: Context, intent: Intent)= goAsync {
    val eventId = intent.getLongExtra("EVENT_ID", -1)
    if (eventId > -1) {
      CerebrumApp.module.calendarRepository.box.get(eventId)?.let {
        context.showNotification(it)
        it.uid?.let { uid ->
          CerebrumApp.module.calendarRepository.remove(eventId, uid)
        }
      }
    }
  }
}

fun BroadcastReceiver.goAsync(
  context: CoroutineContext = EmptyCoroutineContext,
  block: suspend CoroutineScope.() -> Unit
) {
  val pendingResult = goAsync()
  @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
  GlobalScope.launch(context) {
    try {
      block()
    } finally {
      pendingResult.finish()
    }
  }
}