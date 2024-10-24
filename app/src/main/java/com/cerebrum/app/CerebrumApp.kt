package com.cerebrum.app

import android.app.Application
import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.TextUnit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cerebrum.app.compose.settings.AppFontSizes
import com.cerebrum.app.ui.theme.fontSizeLarge
import com.cerebrum.app.ui.theme.fontSizeMedium
import com.cerebrum.app.ui.theme.fontSizeSmall
import com.cerebrum.data.ApiClient
import com.cerebrum.data.DiagnosticMakeFileResponse
import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.Langs
import com.cerebrum.data.objectbox.ObjectBox
import com.cerebrum.data.repositories.ArticlesRepository
import com.cerebrum.data.repositories.AuthRepository
import com.cerebrum.data.repositories.CalendarRepository
import com.cerebrum.data.repositories.PhonebookRepository
import com.cerebrum.data.repositories.TestRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CerebrumApp : Application() {
  companion object {
    lateinit var module: Module
      private set
  }

  override fun onCreate() {
    super.onCreate()
    val settings = AppPreference(this)
    module = Module(settings, this)
    ObjectBox.init(this)
    createChannel()
  }
}

class AppPreference(
  private val context: Context
) : IAuthStore {

  private val TOKEN = stringPreferencesKey("auth_token")
  private val LOGIN = stringPreferencesKey("current_login")
  private val DIAGNOSTIC_TEST_FILENAME = stringPreferencesKey("diagnostic_test_filename")
  private val DIAGNOSTIC_TEST_IS_READY = booleanPreferencesKey("diagnostic_test_is_ready")
  private val DIAGNOSTIC_TEST_DATE = longPreferencesKey("diagnostic_test_date")
  private val APP_FONT_SIZE = intPreferencesKey("app_font_size")

  override fun getLang(): Langs {
    if (Locale.current.language == "en") return Langs.ENG
    return Langs.RU
  }

  override fun hasCredentials(): Boolean = runBlocking {
    val login = context.dataStore.data.map {
      it[LOGIN]
    }.firstOrNull()
    val token = context.dataStore.data.map {
      it[TOKEN]
    }.firstOrNull()
    return@runBlocking !(login.isNullOrEmpty() || token.isNullOrEmpty())
  }

  override fun setLogin(value: String): Unit = runBlocking {
    context.dataStore.edit { settings ->
      settings[LOGIN] = value
    }
  }

  override fun getLogin() = runBlocking {
    context.dataStore.data.map {
      it[LOGIN]
    }.firstOrNull() ?: "NO DATA"
  }

  override fun setToken(value: String): Unit = runBlocking {
    context.dataStore.edit { settings ->
      settings[TOKEN] = value
    }
  }

  override fun getToken(): String {
   return runBlocking {
      context.dataStore.data.map {
        it[TOKEN]
      }.firstOrNull() ?: throw Exception("No auth token")
    }
  }

  override fun clearAuth() {
    setToken("")
  }

  fun getAppFontSize(): AppFontSizes {
    return runBlocking {
      val value = context.dataStore.data.map {
        it[APP_FONT_SIZE]
      }.firstOrNull()
      if (value == AppFontSizes.MEDIUM.value) return@runBlocking AppFontSizes.MEDIUM
      if (value == AppFontSizes.LARGE.value) return@runBlocking AppFontSizes.LARGE
      return@runBlocking AppFontSizes.SMALL
    }
  }

  fun setAppFontSize(size: AppFontSizes): Unit = runBlocking {
    context.dataStore.edit { settings ->
      settings[APP_FONT_SIZE] = size.value
    }
  }

  fun setDiagnosticFileStatus(
    value: DiagnosticMakeFileResponse
  ): Pair<DiagnosticMakeFileResponse, Long> = runBlocking {
    val date = Calendar.getInstance().timeInMillis
    context.dataStore.edit { settings ->
      settings[DIAGNOSTIC_TEST_FILENAME] = value.fileName
      settings[DIAGNOSTIC_TEST_IS_READY] = value.isReady
      settings[DIAGNOSTIC_TEST_DATE] = date
    }
    return@runBlocking Pair(value, date)
  }

  fun getDiagnosticFileStatus(): Pair<DiagnosticMakeFileResponse?, Long>? = runBlocking {
    val isReady = context.dataStore.data.map {
      it[DIAGNOSTIC_TEST_IS_READY]
    }.firstOrNull()
    val fileName = context.dataStore.data.map {
      it[DIAGNOSTIC_TEST_FILENAME]
    }.firstOrNull()
    val date = context.dataStore.data.map {
      it[DIAGNOSTIC_TEST_DATE]
    }.firstOrNull() ?: 0
    isReady?.let { _isReady ->
      fileName?.let { _fileName ->
        return@runBlocking Pair(DiagnosticMakeFileResponse(
          isReady = _isReady,
          fileName = _fileName
        ), date)
      }
    }
    return@runBlocking null
  }

  fun clearDiagnosticFileStatus() = runBlocking {
    context.dataStore.edit { settings ->
      settings.remove(DIAGNOSTIC_TEST_FILENAME)
      settings.remove(DIAGNOSTIC_TEST_IS_READY)
      settings.remove(DIAGNOSTIC_TEST_DATE)
    }
  }
}


class Module(
  val preference: AppPreference,
  private val context: Context
) {
  val token get() = preference.getToken()
  val language get() = preference.getLang()
  val currentLogin get() = preference.getLogin()

  val hasCredentials get() = preference.hasCredentials()

  val downloader: Downloader by lazy {
    Downloader(context)
  }

  private val apiClient: IWebClient by lazy {
    ApiClient.createClient()
  }

  val authRepository : AuthRepository by lazy {
    AuthRepository(apiClient, preference)
  }

  val testRepository : TestRepository by lazy {
    TestRepository(apiClient, preference)
  }

  val phonebookRepository: PhonebookRepository by lazy {
    PhonebookRepository(apiClient, preference)
  }

  val articlesRepository: ArticlesRepository by lazy {
    ArticlesRepository(apiClient, preference)
  }

  val calendarRepository: CalendarRepository by lazy {
    CalendarRepository(apiClient, preference)
  }

  fun logout() {
    calendarRepository.clearLocalOnLogout()
    preference.setLogin("")
    preference.clearAuth()
    preference.clearDiagnosticFileStatus()
  }

  suspend fun loadEvents() {
    CerebrumApp.module.calendarRepository
      .load(language.value)
      .forEach {
        context.scheduleNotification(it)
      }
  }

}