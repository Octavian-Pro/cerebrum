package com.cerebrum.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun SystemBroadcastReceiver(
  systemAction: String,
  onSystemEvent: (context: Context?, intent: Intent?) -> Unit
) {
  val currentContext = LocalContext.current
  val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)
  DisposableEffect(currentContext, systemAction) {
    val intentFilter = IntentFilter(systemAction)
    val broadcast = object: BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        currentOnSystemEvent(context, intent)
      }
    }
    currentContext.registerReceiver(broadcast, intentFilter)
    onDispose {
      currentContext.unregisterReceiver(broadcast)
    }
  }
}