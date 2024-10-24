package com.cerebrum.data

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.cerebrum.data.test", appContext.packageName)
  }

  fun timezoneTest() {
    val mskTz = TimeZone.getTimeZone("GMT+03:00") // msk default
    val time = "2023-12-29 04:05:06"
    val formatter = DateTimeFormatter
      .ofPattern("YYYY-MM-dd HH:mm:ss")
      .withZone(ZoneId.of("Europe/Moscow"))

    val tztm = ZonedDateTime.parse(time, formatter)

  }
}