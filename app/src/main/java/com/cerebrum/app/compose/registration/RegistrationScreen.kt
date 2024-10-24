package com.cerebrum.app.compose.registration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.DynamicQuestionClickableRow
import com.cerebrum.app.compose.ErrorField
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.compose.formatPhone
import com.cerebrum.app.compose.phonebook.InputField
import com.cerebrum.app.compose.phonebook.PhoneNumberTextField
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.colorError
import com.cerebrum.app.ui.theme.colorGray100
import com.cerebrum.app.ui.theme.colorGreenDark
import com.cerebrum.app.ui.theme.getFontSize
import com.cerebrum.app.ui.theme.link
import kotlinx.coroutines.async

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
  navController: NavController
) {

  val vm = viewModel<RegistrationViewModel>()
  LaunchedEffect(key1 = vm.isSuccess) {
    if (vm.isSuccess) {
      navController.navigate("main")
    }
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = stringResource(id = R.string.registration),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .padding(top = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Wizard(vm)
      Spacer(modifier = Modifier.height(64.dp))
      DynamicQuestionClickableRow(
        questionId = R.string.already_have_an_account_q,
        answerId = R.string.login_,
        onClick = {
          navController.navigate("login") {
            popUpTo("login") {
              inclusive = true
            }
          }
        }
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Wizard(
  vm : RegistrationViewModel
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    pageCount = {2}
  )
  HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxWidth(),
    beyondBoundsPageCount = 2,
    userScrollEnabled = false
  ) { page ->
    if (page == 0) {
      PageLoginData(vm)
    }
    if (page == 1) {
      PagePersonalData(vm)
    }
    Spacer(modifier = Modifier.width(32.dp))
  }
  Row(
    Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      //.align(Alignment.BottomCenter)
      .padding(bottom = 8.dp),
    horizontalArrangement = Arrangement.Center
  ) {
    repeat(pagerState.pageCount) { iteration ->
      val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
      Box(
        modifier = Modifier
          .padding(2.dp)
          .clip(CircleShape)
          .background(color)
          .size(8.dp)
      )
    }
  }
  Spacer(modifier = Modifier.height(32.dp))
  val buttonsPagerState = rememberPagerState(pageCount = {2})
  HorizontalPager(
    state = buttonsPagerState,
    modifier = Modifier.fillMaxWidth(),
    beyondBoundsPageCount = 2,
    userScrollEnabled = false
  ) { page ->
    Column(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AnimatedVisibility(
        visible = vm.isLoading,
        modifier = Modifier
          .padding(16.dp)
      ) {
        CircularProgressIndicator()
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        if (page == 0) {
          AppButton(
            caption = stringResource(R.string.next),
            onClick = {
              vm.checkLoginPage()
              if (vm.hasErrors()) return@AppButton
              scope.async {
                pagerState.animateScrollToPage(
                  page = 1,
                  animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
              }
              scope.async {
                buttonsPagerState.animateScrollToPage(
                  page = 1,
                  animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
              }
            },
            width = 176
          )
        }
        if (page == 1) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            AppButton(
              caption = stringResource(id = R.string.back),
              width = 142,
              enabled = vm.isLoading.not(),
              color = colorGray100,
              onClick = {
                scope.async {
                  pagerState.animateScrollToPage(
                    page = 0,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                  )
                }
                scope.async {
                  buttonsPagerState.animateScrollToPage(
                    page = 0,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                  )
                }
              }
            )
            AppButton(
              caption = stringResource(id = R.string.registration),
              enabled = vm.isLoading.not(),
              onClick = {
                vm.checkPersonalDataPage()
                if (vm.hasErrors()) return@AppButton
                vm.submit()
              },
              width = 176
            )
          }
        }
      }
    }
  }
}

@Composable
fun PageLoginData(
  vm : RegistrationViewModel
) {
  WizardCard {
    Column(
      modifier = Modifier
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      /*SimpleTextField(
        value = vm.email,
        label = "Email",
        isError = vm.emailIsError,
        onValueChange = {
          vm.setEmailValue(it)
        })*/
      InputField(
        caption = stringResource(R.string.login),
        placeholder = stringResource(R.string.login),
        value = vm.login,
        isError = vm.loginIsError,
        bottomSpace = false,
        onValueChange = { vm.setLoginValue(it) }
      )
      PhoneNumberTextField(
        caption = stringResource(R.string.phont),
        placeholder = "+7 999 123-45-67",
        value = vm.phone,
        isError = vm.phoneIsError,
        bottomSpace = false,
        onValueChange = { vm.phone = formatPhone(it) }
      )
      InputField(
        value = vm.password,
        caption = stringResource(id = R.string.password),
        isError = vm.passwordIsError,
        bottomSpace = false,
        onValueChange = {
          vm.setPasswordValue(it)
        })
      InputField(
        value = vm.passwordRe,
        caption = stringResource(R.string.password_confirm),
        isError = vm.passwordReIsError,
        bottomSpace = false,
        onValueChange = {
          vm.setPasswordReValue(it)
        })
      ErrorField(errors = vm.errors)
    }
  }
}

@Composable
fun PagePersonalData(
  vm : RegistrationViewModel
) {
  WizardCard {
    Column(
      modifier = Modifier
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      InputField(
        caption = stringResource(R.string.surname),
        value = vm.secondName,
        isError = vm.secondNameIsError,
        readOnly = vm.isLoading,
        bottomSpace = false,
        onValueChange = {
          vm.setSecondNameValue(it)
        }
      )
      InputField(
        caption = stringResource(R.string.name),
        value = vm.name,
        isError = vm.nameIsError,
        readOnly = vm.isLoading,
        bottomSpace = false,
        onValueChange = {
          vm.setNameValue(it)
        })
      InputField(
        caption = stringResource(R.string.middle_name),
        value = vm.surname,
        isError = vm.surnameIsError,
        readOnly = vm.isLoading,
        bottomSpace = false,
        onValueChange = {
          vm.setSurnameValue(it)
        })
      BirthdayField(
        caption = stringResource(R.string.birthday),
        value = vm.formattedDate(),
        readOnly = vm.isLoading,
        onValueChange = {
          vm.setBirthday(it)
        })
      ErrorField(errors = vm.errors)
    }
  }
}

@Composable
fun WizardCard(
  content: @Composable () -> Unit
) {
  Box(modifier = Modifier
    .fillMaxWidth()
    .padding(16.dp)
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
        .padding(16.dp, 32.dp, 16.dp, 16.dp),
      contentAlignment = Alignment.Center
    ) {
      content()
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayField(
  caption: String,
  value : String,
  onValueChange : (dateInMs : Long) -> Unit,
  readOnly: Boolean
) {

  val birthPickerState = rememberDatePickerState()
  var showDatePicker by remember {
    mutableStateOf(false)
  }
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
      readOnly = true,
      value = value,
      trailingIcon = {
        IconButton(
          onClick = {
            if (readOnly) return@IconButton
            showDatePicker = true
          }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_date),
            contentDescription = null
          )
        }
      },
      onValueChange = {

      })
  }

  if (showDatePicker) {
    DatePickerDialog(
      onDismissRequest = {
        showDatePicker = false
      },
      confirmButton = {
        TextButton(onClick = {
          showDatePicker = false
          birthPickerState.selectedDateMillis?.let {
            onValueChange(it)
          }
        }) {
          Text(text = stringResource(R.string.confirm))
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showDatePicker = false
        }) {
          Text(text = stringResource(id = R.string.cancel))
        }
      }
    ) {
      DatePicker(state = birthPickerState)
    }
  }
}



@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
  CerebrumTheme {
    RegistrationScreen(rememberNavController())
  }
}