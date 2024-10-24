package com.cerebrum.app.compose.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cerebrum.app.R
import com.cerebrum.app.compose.SpacerHeight
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.data.ReminderTypes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomDialog(
  onDismiss: () -> Unit,
  onSelect: (type: ReminderTypes) -> Unit,
) {
  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  ModalBottomSheet(
    onDismissRequest = {
      onDismiss()
    },
    sheetState = sheetState
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      BottomSheetButton(
        text = stringResource(R.string.taking_medication),
        icon = R.drawable.ic_btn_pills,
        onClick = {
          onSelect(ReminderTypes.PILLS)
          scope.launch { sheetState.hide() }.invokeOnCompletion {
            onDismiss()
          }
        }
      )
      BottomSheetButton(
        text = stringResource(R.string.do_the_exercise),
        icon = R.drawable.ic_btn_happy,
        onClick = {
          onSelect(ReminderTypes.EXERCISE)
          scope.launch { sheetState.hide() }.invokeOnCompletion {
            onDismiss()
          }
        }
      )
      BottomSheetButton(
        text = stringResource(R.string.event),
        icon = R.drawable.ic_btn_flag,
        onClick = {
          onSelect(ReminderTypes.DOCTOR_OR_EVENT)
          scope.launch { sheetState.hide() }.invokeOnCompletion {
            onDismiss()
          }
        }
      )
      SpacerHeight(height = 64.dp)
    }
  }
}

@Preview
@Composable
fun PreviewCalendarBottomDialog() {
  CerebrumTheme {
    Surface {
      CalendarBottomDialog(
        onDismiss = {},
        onSelect = {}
      )
    }
  }
}