package com.cerebrum.app.compose.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cerebrum.app.CerebrumApp
import com.cerebrum.app.compose.SpacerWidth
import com.cerebrum.app.compose.settings.AppFontSizes
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.fontSizeLarge
import com.cerebrum.app.ui.theme.fontSizeMedium
import com.cerebrum.app.ui.theme.fontSizeSmall
import com.cerebrum.app.ui.theme.getFontSize


val buttonColors = arrayListOf(
  Color(0xFF0EB83E),
  Color(0xFFB80E0E),
)

val fourColours = arrayListOf(
  Color(0xFF0EB83E),
  Color(0xFF578A28),
  Color(0xFF9A3418 ),
  Color(0xFFB80E0E),
)

fun getColorForButtonByIndex(i: Int, total: Int): Color {
  if (i >= total || total < 1) throw Exception("Incorrect indexes")
  val r = (buttonColors[1].red - buttonColors[0].red) / (total - 1)
  val g = (buttonColors[1].green - buttonColors[0].green) / (total - 1)
  val b = (buttonColors[1].blue - buttonColors[0].blue) / (total - 1)
  return Color(
    buttonColors[0].red + (r * i),
    buttonColors[0].green + (g * i),
    buttonColors[0].blue + (b * i)
  )
  /*
  return when {
    i == 0 -> fourColours[0]
    i == 1 -> if (total == 2) fourColours[3] else fourColours[1]
    i == 2 -> fourColours[2]
    i == 3 -> fourColours[3]
    else -> fourColours[0]
  }*/
}

fun isEnabledButton(
  answered: String?,
  answer: String
) : Boolean {
  if (answered.isNullOrEmpty()) return true
  if (answer == answered) return true
  return false
}

@Composable
fun ShowAnswers(
  answers: List<String>,
  answered: String? = null,
  onClicked: (String) -> Unit
) {
  if (answers.size < 2) return
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    var i = 0
    while (i < answers.size) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        TestButton(
          enabled = isEnabledButton(answered, answers[i]),
          color = getColorForButtonByIndex(i, answers.size),
          caption = answers[i],
          clickable = answered.isNullOrEmpty().not(),
          onClicked = onClicked
        )
        SpacerWidth(width = 16.dp)
        i++
        if (i < answers.size) {
          TestButton(
            enabled = isEnabledButton(answered, answers[i]),
            color = getColorForButtonByIndex(i, answers.size),
            caption = answers[i],
            clickable = answered.isNullOrEmpty().not(),
            onClicked = onClicked
          )
          i++
        } else {
          Spacer(modifier = Modifier.weight(0.33f))
        }
      }
    }
  }
}

@Composable
fun RowScope.TestButton(
  enabled: Boolean,
  clickable: Boolean,
  color: Color,
  caption: String,
  onClicked: (String) -> Unit,
) {
  OutlinedButton(
    modifier = Modifier
      .clickable(
        enabled = clickable,
        onClick = {

        }
      )
      .weight(0.33f)
      .height(44.dp)
      .alpha(if (enabled) 1.0f else 0.2f),
    border = BorderStroke(
      width = 2.dp,
      color =  color
    ),
    enabled = enabled,
    shape = CutCornerShape(0.dp, 0.dp, 12.dp, 0.dp),
    onClick = {
      onClicked(caption)
    }
  ) {
    Text(
      text = caption,
      fontSize = getFontSize(),
      color = color
    )
  }
}

@Preview
@Composable
fun PreviewShowAnswers() {
  CerebrumTheme {
    Surface {
      ShowAnswers(
        answers = listOf("Yes", "No", "Maybe"),
        answered = null,
        onClicked = {}
      )
    }
  }
}