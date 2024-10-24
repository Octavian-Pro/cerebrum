package com.cerebrum.app.compose.calendar

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.CHANNEL_ID
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.removeAlarm
import com.cerebrum.app.scheduleNotification
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.getFontSize
import com.cerebrum.data.CalendarResponse
import com.cerebrum.data.ReminderTypes
import com.cerebrum.data.objectbox.entities.CalendarEvent
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle


fun Context.hasNotificationPermission(): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    ContextCompat.checkSelfPermission(
      this,
      Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
  } else {
    true
  }
}

@SuppressLint("RememberReturnType")
@Composable
fun CalendarScreen(
  navController : NavController
) {
  val context = LocalContext.current
  var hasNotificationPermission by remember {
    mutableStateOf(context.hasNotificationPermission())
  }
  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted ->
      hasNotificationPermission = isGranted
      if (!isGranted) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
//
//        }
      }
    }
  )

  LaunchedEffect(key1 = Unit) {
    if (!hasNotificationPermission) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  val currentMonth = remember { YearMonth.now() }
  val startMonth = remember { currentMonth.minusMonths(500) }
  val endMonth = remember { currentMonth.plusMonths(500) }
  var selection by remember { mutableStateOf<CalendarDay?>(CalendarDay(LocalDate.now(), DayPosition.MonthDate)) }
  val daysOfWeek = remember { daysOfWeek() }

  val vm = viewModel<CalendarViewModel>()

  val currentEvents by vm.currentEvents.collectAsStateWithLifecycle()

  val state = rememberCalendarState(
    startMonth = startMonth,
    endMonth = endMonth,
    firstVisibleMonth = currentMonth,
    firstDayOfWeek = daysOfWeek.first(),
    outDateStyle = OutDateStyle.EndOfGrid,
  )

  val newEvent by vm.newEvent.collectAsStateWithLifecycle()
  val coroutineScope = rememberCoroutineScope()
  val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

  LaunchedEffect(key1 = newEvent) {
    newEvent?.let {
      context.scheduleNotification(it)
    }
  }

  LaunchedEffect(visibleMonth) {
    // Clear selection if we scroll to a new month.
    // selection = null
    if (visibleMonth.yearMonth != currentMonth) {
      selection = null
    }
    vm.setCurrentMonth(visibleMonth.yearMonth)
  }

  LaunchedEffect(key1 = selection) {
    selection?.let {
      vm.setCurrentDay(it.date)
    }
    if (selection == null) {
      vm.clearSelection()
    }
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = "Календарь",
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      CompositionLocalProvider(LocalContentColor provides Color.Black) {
        SimpleCalendarTitle(
          modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 12.dp),
          currentMonth = visibleMonth.yearMonth,
          goToPrevious = {
            coroutineScope.launch {
              state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
            }
          },
          goToNext = {
            coroutineScope.launch {
              state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
            }
          },
        )

        val localDensity = LocalDensity.current
        var calHeight by remember {
          mutableStateOf(0.dp)
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          HorizontalCalendar(
            modifier = Modifier
              .wrapContentWidth()
              .onGloballyPositioned {
                calHeight = with(localDensity) { it.size.height.toDp() }
              },
            state = state,
            monthHeader = {
              MonthHeader(
                modifier = Modifier.padding(vertical = 8.dp),
                daysOfWeek = daysOfWeek,
              )
            },
            dayContent = { day ->
              CompositionLocalProvider(LocalRippleTheme provides CerebrumRippleTheme) {
                Day(
                  day = day,
                  isSelected = selection == day,
                  hasPills = vm.hasPills(day.date),
                  hasExercises = vm.hasExercise(day.date),
                  hasDoctors = vm.hasDoctors(day.date)
                ) { clicked ->
                  selection = clicked
                }
              }
            }
          )
        }
      } // calendar
      EventList(
        modifier = Modifier.weight(1f),
        events = currentEvents,
        onDeleteClick = { event ->
          context.removeAlarm(event)
          event.uid?.let {
            vm.removeReminder(event.id, it)
          }
        }
      )

      var showBottomDialog by remember {
        mutableStateOf(false)
      }
      var showTimePicker by remember {
        mutableStateOf(false)
      }
      var selectedReminderType by remember {
        mutableStateOf<ReminderTypes?>(null)
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        AppButton(
          caption = stringResource(R.string.add_event),
          color = colorGreenDark,
          width = 249,
          enabled = hasNotificationPermission,
          onClick = { showBottomDialog = true }
        )
      }
      //val context = LocalContext.current
      if (showBottomDialog) {
        CalendarBottomDialog(
          onDismiss = {
            showBottomDialog = false
          },
          onSelect = {
            selectedReminderType = it
            showTimePicker = true
          }
        )
      }

      if (showTimePicker) {
        SelectTimeDialog(
          showDescription = selectedReminderType == ReminderTypes.DOCTOR_OR_EVENT,
          onDismiss = {
            showTimePicker = false
            selectedReminderType = null
          },
          onConfirm = { hour, minute, description ->
            val v = selectedReminderType
            v?.let { type ->
              selection?.let { day ->
                vm.addReminder(type, day.date, hour, minute, description)
                showTimePicker = false
                selectedReminderType = null
              }
            }
          }
        )
      }

    }
  }
}

//@Composable
//fun TimeTest() {
//  // val mskTz = TimeZone.getTimeZone("GMT+03:00") // msk default
//  val time = "2023-12-29 04:05:06"
//  val repo = CerebrumApp.module.calendarRepository
//  val tztm = repo.fromServerToLocal(CalendarResponse(
//    id = "",
//    name = "",
//    category = "Упражнения",
//    date = time,
//  ))
//  Column(
//    modifier = Modifier.fillMaxWidth()
//  ) {
//    Text(text = "From: ${time}")
//    Text(text = "To: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tztm)}")
//  }
//}

fun Context.showNotification(event: CalendarEvent) {
  val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
    .setContentText(
      when (event.reminderType) {
        ReminderTypes.PILLS -> getString(R.string.it_s_time_to_take_your_medicine)
        ReminderTypes.DOCTOR_OR_EVENT -> event.description
          ?: getString(R.string.event_without_description)

        ReminderTypes.EXERCISE -> getString(R.string.time_to_do_the_exercises)
        else -> getString(R.string.not_indicated)
      }
    )
    .setContentTitle(getString(R.string.notification))
    .setSmallIcon(R.drawable.ic_notification)
    .setColor(ContextCompat.getColor(this, R.color.white))
    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    .setGroup(
      when (event.reminderType) {
        ReminderTypes.PILLS -> "com.cerebrum.notifications.PILLS"
        ReminderTypes.DOCTOR_OR_EVENT -> "com.cerebrum.notifications.DOCTOR"
        ReminderTypes.EXERCISE -> "com.cerebrum.notifications.EXERCISE"
        else -> "UNSPECIFIED"
      }
    )
    .setAutoCancel(true)
    .build()
  manager.notify(
    event.id.toInt(),
    notification
  )
}


@Composable
fun BottomSheetButton(
  text: String,
  @DrawableRes icon: Int,
  onClick: () -> Unit
) {
  TextButton(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
    ) {
      Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = icon),
        tint = Color.Black,
        contentDescription = null
      )
      Text(
        text = text,
        fontSize = getFontSize(),
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black,
        textAlign = TextAlign.Center
      )
    }
  }
}

private object CerebrumRippleTheme : RippleTheme {
  @Composable
  override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Gray, lightTheme = false)

  @Composable
  override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(Color.Gray, lightTheme = false)
}

@Preview
@Composable
fun PreviewCalendarScreen() {
  val navController = rememberNavController()
  CerebrumTheme {
    Surface {
      CalendarScreen(navController)
    }
  }
}


fun YearMonth.displayText(short: Boolean = false): String {
  return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
  val style = if (short) TextStyle.SHORT else TextStyle.FULL
  return getDisplayName(style, Locale.getDefault())
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
  return getDisplayName(TextStyle.SHORT, Locale.getDefault()).let { value ->
    if (uppercase) value.uppercase(Locale.getDefault()) else value
  }
}

/**
 * Alternative way to find the first fully visible month in the layout.
 *
 * @see [rememberFirstVisibleMonthAfterScroll]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
  val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
  // Only take non-null values as null will be produced when the
  // list is mid-scroll as no index will be completely visible.
  LaunchedEffect(state) {
    snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
      .filterNotNull()
      .collect { month -> visibleMonth.value = month }
  }
  return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
  get() {
    val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
    return if (visibleItemsInfo.isEmpty()) {
      emptyList()
    } else {
      val lastItem = visibleItemsInfo.last()
      val viewportSize = this.viewportEndOffset + this.viewportStartOffset
      if (lastItem.offset + lastItem.size > viewportSize) {
        visibleItemsInfo.removeLast()
      }
      val firstItem = visibleItemsInfo.firstOrNull()
      if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
        visibleItemsInfo.removeFirst()
      }
      visibleItemsInfo.map { it.month }
    }
  }

