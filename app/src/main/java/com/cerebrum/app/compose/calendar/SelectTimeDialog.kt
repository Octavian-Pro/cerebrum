package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.compose.SpacerWidth
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeDialog(
  onDismiss: () -> Unit,
  onConfirm: (
    hour: Int,
    minute: Int,
    description: String?
  ) -> Unit,
  showDescription: Boolean
) {

  var description by remember {
    mutableStateOf("")
  }
  var descriptionIsError by remember {
    mutableStateOf(false)
  }


  var timePickerState = rememberTimePickerState(
    initialHour = LocalTime.now().hour,
    is24Hour = true
  )
  BasicAlertDialog(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .padding(16.dp),

    onDismissRequest = { onDismiss() },
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.specify_a_reminder_time),
        color = colorGreenDark,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
      SpacerHeight(height = 8.dp)
      TimePicker(state = timePickerState)
      SpacerHeight(height = 8.dp)
      if (showDescription) {
        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = description,
          isError = descriptionIsError,
          placeholder = {
            Text(stringResource(R.string.description))
          },
          onValueChange = {
            description = it
          }
        )
      }
      SpacerHeight(height = 32.dp)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        AppButton(
          caption = stringResource(R.string.cancel),
          autoWidth = true,
          color = Color(0xFFB80E0E),
          onClick = {
            onDismiss()
          })
        SpacerWidth(width = 16.dp)
        AppButton(
          caption = stringResource(R.string.save),
          color = Color(0xFF0EB83E),
          onClick = {
            descriptionIsError = false
            if (showDescription && description.isNullOrEmpty()) {
              descriptionIsError = true
            } else {
              onConfirm(
                timePickerState.hour,
                timePickerState.minute,
                description
              )
            }
          },
          autoWidth = true,
        )
      }
    }
  }
}

@Preview
@Composable
fun PreviewSelectTimeDialog() {
  CerebrumTheme {
    Surface {
      SelectTimeDialog(
        onDismiss = {

        },
        onConfirm = { h, m, d ->

        },
        showDescription = true
      )
    }
  }
}