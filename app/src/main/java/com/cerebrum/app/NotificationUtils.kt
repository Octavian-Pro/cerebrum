package com.cerebrum.app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.cerebrum.data.objectbox.entities.CalendarEvent
import okhttp3.internal.notify
import java.util.Calendar

const val CHANNEL_ID = "cerebrum_notifications"

  // runs in app.onCreate
fun Context.createChannel() {
  // for Build.VERSION_CODES.O and upper
  val channel = NotificationChannel(
    CHANNEL_ID,
    "Напоминания",
    NotificationManager.IMPORTANCE_HIGH
  )
  channel.enableVibration(true)
  channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
  val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  manager.createNotificationChannel(channel)
}

fun Context.scheduleNotification(event: CalendarEvent) {
  val calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, event.year)
    set(Calendar.MONTH, event.month - 1)
    set(Calendar.DAY_OF_MONTH, event.day)
    set(Calendar.HOUR_OF_DAY, event.hour)
    set(Calendar.MINUTE, event.minute)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
  }
  if (Calendar.getInstance().before(calendar)) {
    val intent = Intent(applicationContext, AlarmReceiver::class.java).apply {
      putExtra("EVENT_ID", event.id)
    }
    val pendingIntent = PendingIntent.getBroadcast(
      applicationContext,
      event.id.toInt(),
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    manager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
  }
}

fun Context.removeAlarm(event: CalendarEvent) {
  val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
  val pending = PendingIntent.getBroadcast(
    this,
    event.id.toInt(),
    Intent(this, AlarmReceiver::class.java),
    PendingIntent.FLAG_IMMUTABLE
  )
  manager.cancel(pending)
}