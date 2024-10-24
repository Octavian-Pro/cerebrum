package com.cerebrum.app.compose.settings

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.rememberMutableStateListOf
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorError
import com.cerebrum.app.ui.theme.colorGrayLight
import com.cerebrum.app.ui.theme.colorGreen100
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val PIN_LENGTH = 4

@Composable
fun SetSecurityCode(
  navController: NavController
) {
  val scope = rememberCoroutineScope()
  val shakeOffsetX = remember { Animatable(0f) }
  val numbers = rememberMutableStateListOf<Int>()
  val isError = remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      text = stringResource(R.string.set_security_code),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Spacer(modifier = Modifier.height(48.dp))
    Text(
      text = stringResource(R.string.enter_security_code),
      color = colorGreen100
    )
    Spacer(modifier = Modifier.height(16.dp))
    EnteredDigits(numbers, shakeOffsetX.value, isError.value)
    DigitalKeyboard(
      onNewNumber = {
        if (numbers.size < PIN_LENGTH) {
          numbers.add(it)
        } else {
          shakeError(scope, shakeOffsetX)
        }
        checkCode(numbers, isError, scope, shakeOffsetX)
      },
      onBackspace = {
        numbers.removeLastOrNull()
        checkCode(numbers, isError, scope, shakeOffsetX)
      }
    )
  }
}
fun checkCode(
  numbers : List<Int>,
  state : MutableState<Boolean>,
  scope: CoroutineScope,
  offset: Animatable<Float, AnimationVector1D>
) {
  if (numbers.size == PIN_LENGTH) {
    var isErrorCode = true
    for (i in 0 until PIN_LENGTH) {
      if (numbers[i] != 1) {
        isErrorCode = false
        break
      }
    }
    state.value = isErrorCode
  } else {
    state.value = false
  }
  if (state.value) {
    shakeError(scope, offset)
  }
}

fun shakeError(
  scope: CoroutineScope,
  offset: Animatable<Float, AnimationVector1D>,
) {
  scope.launch {
    offset.animateTo(
      targetValue = 0f,
      animationSpec = shakeKeyframes,
    )
  }

  /*
  view?.let {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      view.performHapticFeedback(HapticFeedbackConstants.REJECT)
    } else {
      view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
  }*/
}

@Composable
fun EnteredDigits(
  numbers: List<Int>,
  offsetX: Float,
  isError : Boolean
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier
        .padding(8.dp)
        .offset(offsetX.dp, 0.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      repeat(PIN_LENGTH) {
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(
              if (isError) {
                colorError
              } else
                if (it < numbers.size)
                  MaterialTheme.colorScheme.primary
                else
                  colorGrayLight
            )
        )
      }
    }
    if (isError) {
      Text(
        text = "Неверный код доступа",
        color = colorError
      )
      Text(
        text = "Пожалуйста, попробуйте снова",
        color = colorError
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
  }
}
@Composable
fun DigitalKeyboard(
  onNewNumber : (value : Int) -> Unit,
  onBackspace : () -> Unit,
) {
  Column(
    modifier = Modifier
      .width(280.dp)
      .height(328.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      DigitButton(1, onClick = onNewNumber)
      DigitButton(2, onClick = onNewNumber)
      DigitButton(3, onClick = onNewNumber)
    }
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      DigitButton(4, onClick = onNewNumber)
      DigitButton(5, onClick = onNewNumber)
      DigitButton(6, onClick = onNewNumber)
    }
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      DigitButton(7, onClick = onNewNumber)
      DigitButton(8, onClick = onNewNumber)
      DigitButton(9, onClick = onNewNumber)
    }
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Box(modifier = Modifier.size(70.dp))
      DigitButton(0, onClick = onNewNumber)
      Button(
        onClick = onBackspace,
        modifier = Modifier.size(70.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Transparent,
          contentColor = Color.Black
        )
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_backspace),
          contentDescription = null
        )
      }
    }
  }
}

@Composable
fun DigitButton(
  number: Int,
  onClick : (value : Int) -> Unit
) {
  Button(
    onClick = { onClick(number) },
    modifier = Modifier.size(70.dp),
    shape = CircleShape,
    contentPadding = PaddingValues(0.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = colorGrayLight,
      contentColor = Color.Black
    )
  ) {
    Text(
      text = number.toString(),
      fontSize = 28.sp
    )
  }
}

private val shakeKeyframes: AnimationSpec<Float> = keyframes {
  durationMillis = 800
  val easing = FastOutLinearInEasing

  // generate 8 keyframes
  for (i in 1..8) {
    val x = when (i % 3) {
      0 -> 4f
      1 -> -4f
      else -> 0f
    }
    x at durationMillis / 10 * i with easing
  }
}

@Preview
@Composable
fun PreviewSetSecurityCode() {
  CerebrumTheme {
    SetSecurityCode(rememberNavController())
  }
}