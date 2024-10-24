package com.cerebrum.app.compose.phonebook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.ErrorField
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.formatPhone
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.colorRedSecondary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddContactScreen(
  navController: NavController
) {
  val vm = viewModel<ContactViewModel>()
  val state by vm.state.collectAsStateWithLifecycle()

  LaunchedEffect(key1 = state.isSaved) {
    if (state.isSaved) {
      navController.navigateUp()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp, 0.dp, 0.dp, 32.dp)
  ) {
    Toolbar(
      text = stringResource(R.string.new_contact),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(32.dp)
    ) {
      InputField(
        caption = "Имя",
        value = vm.name,
        isError = vm.nameIsError,
        onValueChange = { vm.name = it }
      )
      InputField(
        caption = stringResource(R.string.doctor_speciality),
        placeholder = stringResource(R.string.speciality),
        value = vm.speciality,
        isError = vm.specialityIsError,
        onValueChange = { vm.speciality = it }
      )
      PhoneNumberTextField(
        caption = stringResource(R.string.phone_number),
        placeholder = "+7 999 123-45-67",
        value = vm.phone,
        isError = vm.phoneIsError,
        onValueChange = { vm.phone = formatPhone(it) }
      )
      ErrorField(errors = vm.errors, emptyHeight = 56.dp)
      AnimatedVisibility(visible = state.isLoading) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.Center
        ) {
          CircularProgressIndicator()
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        AppButton(
          enabled = state.isLoading.not(),
          caption = stringResource(id = R.string.cancel),
          onClick = {
            navController.navigateUp()
          },
          width = 124,
          height = 56,
          color = colorRedSecondary
        )
        AppButton(
          enabled = state.isLoading.not(),
          caption = if (vm.isNew()) { stringResource(id = R.string.add) } else { stringResource(id = R.string.save) },
          onClick = { vm.submit() },
          width = 124,
          height = 56,
        )
      }
    }
  }
}

@Composable
fun InputField(
  caption: String,
  value: String,
  isError: Boolean,
  onValueChange: (String) -> Unit,
  placeholder: String? = null,
  bottomSpace: Boolean = true,
  readOnly: Boolean = false,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Text(
      text = caption,
      color = colorGreenDark,
      modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
    )
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = value,
      isError = isError,
      readOnly = readOnly,
      placeholder = {
        Text(
          text = placeholder ?: caption,
          color = Color(0xFF808080)
        )
      },
      onValueChange = onValueChange
    )
    if (bottomSpace) Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PhoneNumberTextField(
  caption : String,
  value: String,
  isError: Boolean,
  onValueChange: (String) -> Unit,
  placeholder : String? = null,
  bottomSpace : Boolean = true,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Text(
      text = caption,
      color = colorGreenDark,
      modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
    )
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
      value = TextFieldValue(value, selection = TextRange(value.length)),
      isError = isError,
      placeholder = {
        Text(
          text = placeholder ?: caption,
          color = Color(0xFF808080)
        )
      },
      onValueChange = {
        onValueChange(it.text)
      }
    )
    if (bottomSpace) Spacer(modifier = Modifier.height(16.dp))
  }
}

@Preview
@Composable
fun PreviewAddContactScreen() {
  CerebrumTheme {
    Surface {
      AddContactScreen(rememberNavController())
    }
  }
}