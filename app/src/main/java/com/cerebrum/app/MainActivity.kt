package com.cerebrum.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.cerebrum.app.compose.CerebrumNavRoot
import com.cerebrum.app.ui.theme.CerebrumTheme

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //WindowCompat.setDecorFitsSystemWindows(window, true)
//    window.setFlags(
//      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//    )
    setContent {
      CerebrumTheme {
        CerebrumNavRoot()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  CerebrumTheme {
    CerebrumNavRoot()
  }
}