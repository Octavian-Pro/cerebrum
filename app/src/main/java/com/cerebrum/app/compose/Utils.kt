package com.cerebrum.app.compose

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.health.connect.datatypes.units.Percentage
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat.startActivity
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.EnumError
import com.cerebrum.app.ErrorsTexts
import com.cerebrum.app.R
import com.cerebrum.app.compose.settings.AppFontSizes
import com.cerebrum.app.ui.theme.colorBlue
import com.cerebrum.app.ui.theme.colorError
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.colorSettingsOption
import com.cerebrum.app.ui.theme.fontSizeLarge
import com.cerebrum.app.ui.theme.fontSizeMedium
import com.cerebrum.app.ui.theme.fontSizeSmall
import com.cerebrum.app.ui.theme.getFontSize
import com.cerebrum.app.ui.theme.link
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import kotlinx.coroutines.launch

@Composable
fun ClickableEmailText(
  modifier: Modifier = Modifier,
  email: String,
  color: Color = Color.Unspecified,
  textStyle: TextStyle = TextStyle.Default
) {
  val context = LocalContext.current

  val annotatedString = buildAnnotatedString {
    pushStringAnnotation("URL", "mailto:$email")
    withStyle(style = SpanStyle(
      color = if (color == Color.Unspecified)
        MaterialTheme.colorScheme.link
      else
        color
    )) {
      append(email)
    }
    pop()
  }
  val style = textStyle.copy(
    fontSize = when(CerebrumApp.module.preference.getAppFontSize()) {
      AppFontSizes.SMALL -> fontSizeSmall
      AppFontSizes.MEDIUM -> fontSizeMedium
      AppFontSizes.LARGE -> fontSizeLarge
    }
  )
  ClickableText(
    modifier = modifier,
    text = annotatedString,
    style = style,
    onClick = { offset ->
      annotatedString.getStringAnnotations("URL", offset, offset)
        .firstOrNull()?.let { annotation ->
          context.openEmailClient(annotation.item)
        }
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
  text : String,
  onBackClicked : () -> Unit,
  isRoot : Boolean = false,
  drawerState: DrawerState? = null,
  textColor : Color? = null,
) {
  val scope = rememberCoroutineScope()
  TopAppBar(
    title = {
      Text(
        text = text,
        color = textColor ?: colorGreenDark
      )
    },
    navigationIcon = {
      IconButton(onClick = {
        if (isRoot) {
          scope.launch {
            drawerState?.apply {
              if (isClosed) open() else close()
            }
          }
        } else {
          onBackClicked()
        }
      }) {
        Icon(
          painter = if (isRoot)
            painterResource(id = R.drawable.ic_menu)
          else
            painterResource(id = R.drawable.ic_toolbar_back),
          contentDescription = "Назад",
          tint = if (isRoot) colorBlue else colorGreenDark //MaterialTheme.colorScheme.primary
        )
      }
    }
  )
}

@Composable
fun AppButton(
  caption: String,
  onClick: () -> Unit,
  width: Int = 162,
  height: Int = 70,
  color: Color = Color.Unspecified,
  enabled: Boolean = true,
  autoWidth: Boolean = false,
) {
  val modifier = if (autoWidth)
    Modifier
      .requiredWidth(IntrinsicSize.Max)
      .height(height.dp)
  else
    Modifier
      .width(width.dp)
      .height(height.dp)

  OutlinedButton(
    modifier = modifier
      .alpha(if (enabled) 1.0f else 0.33f),
    border = BorderStroke(
      width = 2.dp,
      color = if (color == Color.Unspecified) MaterialTheme.colorScheme.primary else color
    ),
    enabled = enabled,
    shape = CutCornerShape(0.dp, 0.dp, 12.dp, 0.dp),
    onClick = { onClick() }
  ) {
    Text(
      text = caption,
      fontSize = getFontSize(),
      color = color
    )
  }
}

val shadowBoxModifier = Modifier
  .fillMaxWidth()
  .shadow(
    elevation = 8.dp,
    RoundedCornerShape(8.dp)
  )
  .border(1.dp, Color(0xFFeaeeeb))
  .background(Color.White)
  .padding(16.dp, 32.dp, 16.dp, 48.dp)

fun shadowBoxModifier(
  paddingStart: Dp = 0.dp,
  paddingTop: Dp = 0.dp,
  paddingEnd: Dp = 0.dp,
  paddingBottom: Dp = 0.dp
) = Modifier
  .fillMaxWidth()
  .shadow(
    elevation = 8.dp,
    RoundedCornerShape(8.dp)
  )
  .border(1.dp, Color(0xFFeaeeeb))
  .background(Color.White)
  .padding(paddingStart, paddingTop, paddingEnd, paddingBottom)

@Composable
fun shadowBoxModifier(
  paddingStartAndEnd : Dp,
  onClick: () -> Unit
) = Modifier
  .fillMaxWidth()
  .clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
    onClick = {
      onClick()
    }
  )
  .shadow(
    elevation = 8.dp,
    RoundedCornerShape(8.dp)
  )
  .border(1.dp, Color(0xFFeaeeeb))
  .background(Color.White)
  .padding(paddingStartAndEnd, 0.dp, paddingStartAndEnd, 0.dp)


@Composable
fun ShadowBox(
  content : @Composable () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(
        elevation = 8.dp,
        RoundedCornerShape(8.dp)
      )
      .border(1.dp, Color(0xFFeaeeeb))
      .background(Color.White)
      .padding(16.dp, 32.dp, 16.dp, 48.dp),
    contentAlignment = Alignment.Center
  ) {
    content()
  }
}

@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
  Box(modifier = Modifier
    .fillMaxWidth()
    .height(height)
    .background(
      brush = Brush.verticalGradient(
        colors = listOf(
          Color.Black.copy(alpha = alpha),
          Color.Transparent,
        )
      )
    )
  )
}

fun Context.openEmailClient(email: String) {
  val intent = Intent(ACTION_SENDTO).apply {
    data = Uri.parse("mailto:$email")
  }
  startActivity(this, intent, null)
}

@Composable
internal fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
  return rememberSaveable(
    saver = listSaver(
      save = { stateList ->
        if (stateList.isNotEmpty()) {
          val first = stateList.first()
          if (!canBeSaved(first)) {
            throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
          }
        }
        stateList.toList()
      },
      restore = { it.toMutableStateList() }
    )
  ) {
    elements.toList().toMutableStateList()
  }
}

@Composable
fun SpacerHeight(
  height: Dp
) {
  Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpacerWidth(
  width: Dp
) {
  Spacer(modifier = Modifier.width(width))
}

@Composable
fun SettingsOptionBox(
  onClick: (value : Boolean) -> Unit,
  content: @Composable () -> Unit
) {
  var state by remember {
    mutableStateOf(false)
  }

  Box(modifier = Modifier
    .fillMaxWidth(0.8f)
    .shadow(
      elevation = 8.dp,
      RoundedCornerShape(8.dp)
    )
    .border(1.dp, colorSettingsOption)
    .background(colorSettingsOption)
    .clickable {
      state = !state
      onClick(state)
    }
    .padding(24.dp, 32.dp, 24.dp, 16.dp)
    .background(colorSettingsOption)
    .fillMaxWidth(.5f)
  ) {
    content()
  }
}

@Composable
fun ErrorField(
  errors: List<EnumError>,
  emptyHeight: Dp = 32.dp
) {
  Column(
    modifier = Modifier
      .defaultMinSize(minHeight = emptyHeight)
      .fillMaxWidth()
  ) {
    errors.forEach { error ->
      Text(
        text = "• ${ErrorsTexts[error] ?: "Неизвестная ошибка"}",
        color = colorError
      )
    }
  }
}

fun callNumber(
  phone: String,
  context: Context
) {
  try {
    Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phone")).let {
      context.startActivity(it)
    }
  } catch (e: Exception) {
    Toast.makeText(context, "Ошибка при вызове", Toast.LENGTH_LONG).show()
  }
}

fun formatPhone(value : String): String {
  val phoneUtil = PhoneNumberUtil.getInstance()
  val result = try {
    val numberProto: Phonenumber.PhoneNumber = phoneUtil.parse(value, "RU")
    phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
  } catch (e: NumberParseException) {
    e.printStackTrace()
    value
  }
  return result
}

fun isValidPhone(value: String): Boolean {
  val phoneUtil = PhoneNumberUtil.getInstance()
  val result = try {
    val phone: Phonenumber.PhoneNumber = phoneUtil.parse(value, "RU")
    PhoneNumberUtil.getInstance().isValidNumber(phone)
  } catch (e: NumberParseException) {
    false
  }
  return result
}

fun Context.openRutube(url: String) {
  Intent(
    Intent.ACTION_VIEW,
    Uri.parse(url)
  ).let {
    startActivity(it)
  }
}
fun Context.openYoutube(id: String) {
  try {
    Intent(
      Intent.ACTION_VIEW,
      Uri.parse("vnd.youtube:$id")
    ).let {
      startActivity(it)
    }
  } catch (e: ActivityNotFoundException) {
    Intent(
      Intent.ACTION_VIEW,
      Uri.parse("http://www.youtube.com/watch?v=$id")
    ).let {
      startActivity(it)
    }
  }
}

@Composable
fun DynamicQuestionClickableRow(
  @StringRes questionId: Int,
  @StringRes answerId: Int,
  onClick: () -> Unit,
  percentage: Float = 0.7f
) {
  var rowSize by remember {
    mutableStateOf(Size.Zero)
  }
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
      .onGloballyPositioned {
        rowSize = it.size.toSize()
      },
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    val maxWidth = LocalDensity.current.run {
      (rowSize.width * percentage).toDp()
    }
    Text(
      modifier = Modifier.widthIn(100.dp, maxWidth),
      text = stringResource(questionId)
    )
    ClickableText(
      text = AnnotatedString(stringResource(answerId)),
      style = TextStyle(
        fontSize = getFontSize(),
        color = MaterialTheme.colorScheme.link,
        textAlign = TextAlign.Center
      ),
      modifier = Modifier
        .width(150.dp)
        .padding(4.dp),
      onClick = {
        onClick()
      }
    )
  }
}