package com.cerebrum.app.compose.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cerebrum.app.R
import com.cerebrum.app.compose.AppButton
import com.cerebrum.app.compose.DynamicQuestionClickableRow
import com.cerebrum.app.compose.ErrorField
import com.cerebrum.app.compose.Toolbar
import com.cerebrum.app.ui.theme.CerebrumTheme
import com.cerebrum.app.ui.theme.link


@Composable
fun LoginScreen(
  navController: NavController
) {

  val vm = viewModel<LoginViewModel>()
  val event by vm.events.observeAsState()

  when (event) {
    LoginEvents.LOGIN_SUCCESS -> {
      navController.navigate("main")
    }
    else -> {}
  }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Toolbar(
      text = stringResource(R.string.login_enter),
      onBackClicked = {
        navController.navigateUp()
      }
    )
    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
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
          .padding(16.dp, 32.dp, 16.dp, 32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.email,
            placeholder = {
              Text("Email")
            },
            onValueChange = {
              vm.setEmailValue(it)
            })
          OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.password,
            placeholder = {
              Text(stringResource(R.string.password))
            },
            onValueChange = {
              vm.setPasswordValue(it)
            })
          ErrorField(errors = vm.errors)
          /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            ClickableText(
              text = AnnotatedString("Забыли пароль?"),
              style = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
              ),
              onClick = {}
            )
          }*/
        }
      }
      Spacer(modifier = Modifier.height(32.dp))
      AnimatedVisibility(
        visible = vm.isLoading,
        modifier = Modifier.padding(16.dp)
      ) {
        CircularProgressIndicator()
      }
      AppButton(
        caption = stringResource(id = R.string.login_enter),
        enabled = vm.isLoading.not(),
        onClick = {
          vm.submit()
        },
        width = 162
      )
      Spacer(modifier = Modifier.height(64.dp))
      DynamicQuestionClickableRow(
        questionId = R.string.not_registered_yet_q,
        answerId = R.string.create_account,
        onClick = {
          navController.navigate("registration") {
            popUpTo("registration") {
              inclusive = true
            }
          }
        },
        percentage = 0.6f
      )/*
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = stringResource(R.string.not_registered_yet_q))
        ClickableText(
          text = AnnotatedString(stringResource(R.string.create_account)),
          style = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.link,
            textAlign = TextAlign.Center
          ),
          modifier = Modifier
            .width(136.dp)
            .padding(4.dp),
          onClick = {
            navController.navigate("registration") {
              popUpTo("registration") {
                inclusive = true
              }
            }
          }
        )
      }*/
    }
    Spacer(modifier = Modifier.weight(1f))
    Image(
      painter = painterResource(id = R.drawable.bottom_rounded),
      contentDescription = null,
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth()
    )
  }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  CerebrumTheme {
    LoginScreen(rememberNavController())
  }
}