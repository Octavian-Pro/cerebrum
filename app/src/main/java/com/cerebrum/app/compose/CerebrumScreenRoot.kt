package com.cerebrum.app.compose

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cerebrum.app.BuildConfig
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.R
import com.cerebrum.app.SystemBroadcastReceiver
import com.cerebrum.app.compose.articles.ArticleScreen
import com.cerebrum.app.compose.articles.ArticlesListScreen
import com.cerebrum.app.compose.calendar.CalendarScreen
import com.cerebrum.app.compose.exercise.ExerciseScreen
import com.cerebrum.app.compose.exercise.ExercisesScreen
import com.cerebrum.app.compose.login.LinkSentToEmailScreen
import com.cerebrum.app.compose.login.LoginScreen
import com.cerebrum.app.compose.login.WelcomeScreen
import com.cerebrum.app.compose.main.MainScreen
import com.cerebrum.app.compose.phonebook.AddContactScreen
import com.cerebrum.app.compose.phonebook.PhoneBookScreen
import com.cerebrum.app.compose.registration.RegistrationScreen
import com.cerebrum.app.compose.registration.ThanksForRegistrationScreen
import com.cerebrum.app.compose.settings.AboutUsScreen
import com.cerebrum.app.compose.settings.AccountInfoScreen
import com.cerebrum.app.compose.settings.ChangeFontSizeScreen
import com.cerebrum.app.compose.settings.ChangeLanguageScreen
import com.cerebrum.app.compose.settings.NotificationsScreen
import com.cerebrum.app.compose.settings.SecurityCode
import com.cerebrum.app.compose.settings.SecurityScreen
import com.cerebrum.app.compose.settings.SetSecurityCode
import com.cerebrum.app.compose.settings.SettingsScreen
import com.cerebrum.app.compose.test.DiagnosticTestScreen
import com.cerebrum.app.compose.test.TestScreen
import com.cerebrum.app.ui.theme.CerebrumTheme
import kotlinx.coroutines.launch
import java.io.File


const val ANIMATION_SPEED_FAST = 150

@Composable
fun NavItem(
  drawerState: DrawerState,
  text: String,
  @DrawableRes iconId: Int,
  onClick: () -> Unit
) {
  val scope = rememberCoroutineScope()
  NavigationDrawerItem(
    label = {
      Text(text)
    },
    icon = {
      Icon(
        painter = painterResource(id = iconId),
        contentDescription = null
      )
    },
    selected = false,
    colors = NavigationDrawerItemDefaults.colors(
      unselectedContainerColor = Color.Transparent,
    ),
    onClick = {
      onClick()
      scope.launch {
        drawerState.close()
      }
    }
  )
}



fun openPdf(
  context: Context,
  filePath: String
) {
  // val od = manager.openDownloadedFile(fileId)
  val file = File(Uri.parse(filePath).path)
  val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
  val i = Intent().apply {
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    action = Intent.ACTION_VIEW
    setDataAndType(uri, "application/pdf");
  }
  context.startActivity(i)
}

fun openImage(
  context: Context,
  filePath: String
) {
  val file = File(Uri.parse(filePath).path)
  val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
  val i = Intent().apply {
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    action = Intent.ACTION_VIEW
    setDataAndType(uri, "image/*");
  }
  context.startActivity(i)
}

@SuppressLint("Range")
@Composable
// @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun CerebrumNavRoot() {
  val navHostController = rememberNavController()
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val startFrom = if (CerebrumApp.module.hasCredentials) Routes.MainScreen else Routes.WelcomeScreen

  SystemBroadcastReceiver(
    systemAction = DownloadManager.ACTION_DOWNLOAD_COMPLETE
  ) { context, intent ->
    if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
      val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
      if (id != -1L) {
        context?.let { ctx ->
          val query = DownloadManager.Query()
          query.setFilterById(id)
          val manager = ctx.getSystemService(DownloadManager::class.java)
          // ctx.grantUriPermission(ctx.applicationContext.packageName, )
          val cursor = manager.query(query)
          if (cursor.moveToFirst()) {
            val colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val colIdIndex = cursor.getColumnIndex(DownloadManager.COLUMN_ID)
            if (colIndex > -1 && colIdIndex > -1) {
              val downloadedPath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
              // val fileId = cursor.getLong(colIdIndex)
              val status = cursor.getInt(colIndex)
              when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                  scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val snack = snackbarHostState.showSnackbar(
                      message = context.resources.getString(R.string.download_complete),
                      actionLabel = context.resources.getString(R.string.open),
                      withDismissAction = true,
                      duration = SnackbarDuration.Long
                    )
                    when (snack) {
                      SnackbarResult.ActionPerformed -> {
                        if (downloadedPath.endsWith(".pdf")) {
                          openPdf(ctx, downloadedPath)
                        }
                        if (downloadedPath.endsWith(".png")) {
                          openImage(ctx, downloadedPath)
                        }
                      }
                      SnackbarResult.Dismissed -> {

                      }
                    }
                  }
                }
                DownloadManager.STATUS_FAILED -> {
                  scope.launch {
                    snackbarHostState.showSnackbar(context.resources.getString(R.string.download_error))
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  Scaffold(
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    }
  ) { paddingValues ->
    NavHost(
      modifier = Modifier.padding(paddingValues),
      navController = navHostController,
      startDestination = startFrom,
      enterTransition = {
        slideInHorizontally(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        ) + fadeIn(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        )
      },
      exitTransition = {
        slideOutHorizontally(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        ) + fadeOut(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        )
      },
      popEnterTransition = {
        slideInHorizontally(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        ) + fadeIn(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        )
      },
      popExitTransition = {
        slideOutHorizontally(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        ) + fadeOut(
          animationSpec = tween(
            durationMillis = ANIMATION_SPEED_FAST,
            easing = FastOutLinearInEasing
          )
        )
      }
    ) {
      composable(Routes.WelcomeScreen) {
        Surface(
          modifier = Modifier
            .background(Color.Green)
            .fillMaxSize()
            .scrollable(
              orientation = Orientation.Vertical,
              state = rememberScrollState()
            )
        ) {
          WelcomeScreen(navHostController)
        }
      }
      composable("registration") {
        RegistrationScreen(navHostController)
      }
      composable("login") {
        LoginScreen(navHostController)
      }
      composable(Routes.MainScreen) {
        MainScreen(navHostController)
      }
      composable("articles") {
        ArticlesListScreen(navHostController)
      }
      composable(
        route = "article/{name}/{fileName}",
        arguments = listOf(
          navArgument("name"){
            type = NavType.StringType
          },
          navArgument("fileName") {
            type = NavType.StringType
          }
        )
      ) {
        ArticleScreen(navHostController)
      }
      composable(Routes.TestScreen) {
        TestScreen(navHostController)
      }
      composable(Routes.DiagnosticTestScreen) {
        DiagnosticTestScreen(navHostController)
      }
      composable(Routes.PhonebookScreen) {
        PhoneBookScreen(navHostController)
      }
      composable(
        route = "phonebook/add/{id}",
        arguments = listOf(
          navArgument("id") {
            type = NavType.LongType
          }
        )
      ) {
        AddContactScreen(navHostController)
      }
      composable("thanks_for_registration") {
        ThanksForRegistrationScreen(navHostController)
      }
      composable(Routes.CalendarScreen) {
        CalendarScreen(navHostController)
      }
      composable("exercises") {
        ExercisesScreen(navHostController)
      }
      composable(
        route = "exercise/{id}",
        arguments = listOf(
          navArgument("id"){
            type = NavType.LongType
          }
        )
      ) {
        ExerciseScreen(navHostController)
      }
      composable("medication_reminder") {
        MedicationReminderScreen(navHostController)
      }

      composable("link_sent_to_email") {
        LinkSentToEmailScreen(navHostController)
      }
      composable("settings") {
        SettingsScreen(navHostController)
      }
      composable("settings-security-code") {
        SecurityScreen(navHostController)
      }
      composable("security_code") {
        SecurityCode(navHostController)
      }
      composable("set-security-code") {
        SetSecurityCode(navHostController)
      }
      composable("about_us") {
        AboutUsScreen(navHostController)
      }
      composable("account_info") {
        AccountInfoScreen(navHostController)
      }
      composable("notifications") {
        NotificationsScreen(navHostController)
      }

      composable("change_language") {
        ChangeLanguageScreen(navHostController)
      }
      composable("change_font_size") {
        ChangeFontSizeScreen(navHostController)
      }
    }
  }
}

@Preview
@Composable
fun PreviewRoot() {
  CerebrumTheme {
    Surface {
      CerebrumNavRoot()
    }
  }
}