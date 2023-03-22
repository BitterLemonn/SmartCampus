package com.lemon.smartcampus.ui.authPage

import android.app.Activity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.WarpLoadingDialog
import com.lemon.smartcampus.ui.widges.popupSnackBar
import com.lemon.smartcampus.utils.HOME_PAGE
import com.lemon.smartcampus.viewModel.auth.AuthViewAction
import com.lemon.smartcampus.viewModel.auth.AuthViewEvent
import com.lemon.smartcampus.viewModel.auth.AuthViewModel
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.delay

@Composable
fun AuthPage(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: AuthViewModel = viewModel()
) {
    val viewStates = viewModel.viewStates
    val state by viewStates.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var needWait by rememberSaveable { mutableStateOf(0) }
    var onLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is AuthViewEvent.TransIntent -> navController?.popBackStack()
                is AuthViewEvent.ShowToast -> {
                    scaffoldState?.let { scaffoldState ->
                        popupSnackBar(coroutineScope, scaffoldState, SNACK_ERROR, it.msg)
                    }
                }
                is AuthViewEvent.AlreadyRequestCode -> needWait = 120
                is AuthViewEvent.ShowLoadingDialog -> onLoading = true
                is AuthViewEvent.DismissLoadingDialog -> onLoading = false
            }
        }
    }
    LaunchedEffect(key1 = needWait) {
        if (needWait > 0) {
            delay(1_000)
            needWait -= 1
        }
    }

    var passwordMode by remember { mutableStateOf(true) }
    var registerMode by remember { mutableStateOf(false) }

    val animateTokenSize by animateFloatAsState(targetValue = if (passwordMode) 1f else 0.4f)
    val animateGetWidth by animateDpAsState(targetValue = if (passwordMode) 0.dp else Dp.Unspecified)
    val animateRegHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else Dp.Unspecified)
    val animateSpacerHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else 20.dp)

    LaunchedEffect(key1 = passwordMode) {
        viewModel.dispatch(AuthViewAction.UpdateToken(""))
        viewModel.dispatch(AuthViewAction.UpdatePassword(""))
    }

    LaunchedEffect(key1 = registerMode) {
        passwordMode = !registerMode
        viewModel.dispatch(AuthViewAction.UpdatePassword(""))
    }

    Box(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        Image(painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = { navController?.popBackStack() }
                )
                .padding(5.dp)
                .size(20.dp))
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.school_logo),
            contentDescription = "school logo",
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight(),
            elevation = 10.dp,
            shape = RoundedCornerShape(5.dp),
            backgroundColor = AppTheme.colors.card
        ) {
            Column {
                Divider(
                    color = AppTheme.colors.schoolBlue,
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "登录",
                            color = if (!isSystemInDarkTheme()) AppTheme.colors.schoolBlue
                            else Color(0xFFEAEAEA),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (!registerMode) Text(
                            text = if (passwordMode) "验证码登录" else "密码登录",
                            color = Color(0xFF746E98),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(indication = rememberRipple(),
                                    interactionSource = MutableInteractionSource(),
                                    onClick = {
                                        passwordMode = !passwordMode
                                    })
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // 手机号
                    OutlinedTextField(value = state.phone,
                        onValueChange = {
                            if (it.length <= 11) viewModel.dispatch(AuthViewAction.UpdatePhone(it))
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = if (!isSystemInDarkTheme()) Color.White
                            else Color(0xFF2F2F2F),
                            cursorColor = AppTheme.colors.schoolBlue,
                            unfocusedBorderColor = AppTheme.colors.textLightColor,
                            focusedBorderColor = AppTheme.colors.schoolBlue,
                            focusedLabelColor = AppTheme.colors.schoolBlue,
                            unfocusedLabelColor = AppTheme.colors.textBlackColor
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 14.sp,
                            color = AppTheme.colors.textBlackColor
                        ),
                        label = { Text(text = "手机号", fontSize = 14.sp) })
                    Spacer(modifier = Modifier.height(20.dp))

                    // 注册密码
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            if (it.length <= 50) viewModel.dispatch(AuthViewAction.UpdatePassword(it))
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = if (!isSystemInDarkTheme()) Color.White
                            else Color(0xFF2F2F2F),
                            cursorColor = AppTheme.colors.schoolBlue,
                            unfocusedBorderColor = AppTheme.colors.textLightColor,
                            focusedBorderColor = AppTheme.colors.schoolBlue,
                            focusedLabelColor = AppTheme.colors.schoolBlue,
                            disabledBorderColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent,
                            unfocusedLabelColor = AppTheme.colors.textBlackColor
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animateRegHeight),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 14.sp,
                            color = AppTheme.colors.textBlackColor
                        ),
                        singleLine = true,
                        label = {
                            Text(text = "密码", fontSize = 14.sp)
                        },
                        enabled = registerMode
                    )
                    Spacer(modifier = Modifier.height(animateSpacerHeight))

                    // 登录密码或者验证码
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(value = if (!passwordMode) state.token else state.password,
                            onValueChange = {
                                if (!passwordMode) {
                                    if (it.length <= 4)
                                        viewModel.dispatch(AuthViewAction.UpdateToken(it))
                                } else {
                                    if (it.length <= 50)
                                        viewModel.dispatch(AuthViewAction.UpdatePassword(it))
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = if (!isSystemInDarkTheme()) Color.White
                                else Color(0xFF2F2F2F),
                                cursorColor = AppTheme.colors.schoolBlue,
                                unfocusedBorderColor = AppTheme.colors.textLightColor,
                                focusedBorderColor = AppTheme.colors.schoolBlue,
                                focusedLabelColor = AppTheme.colors.schoolBlue,
                                unfocusedLabelColor = AppTheme.colors.textBlackColor
                            ),
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.fillMaxWidth(animateTokenSize),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                            ),
                            visualTransformation = if (passwordMode) PasswordVisualTransformation()
                            else VisualTransformation.None,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 14.sp,
                                color = AppTheme.colors.textBlackColor
                            ),
                            singleLine = true,
                            label = {
                                Text(text = if (passwordMode) "密码" else "验证码", fontSize = 14.sp)
                            })
                        Button(
                            onClick = { viewModel.dispatch(AuthViewAction.RequestToken) },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, color = AppTheme.colors.textLightColor),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            modifier = Modifier
                                .width(animateGetWidth)
                                .widthIn(min = 120.dp),
                            enabled = needWait == 0
                        ) {
                            Text(
                                text = if (needWait == 0) "获取验证码" else "${needWait}s",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                maxLines = 1,
                                color = AppTheme.colors.textLightColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (registerMode) viewModel.dispatch(AuthViewAction.Register)
                            else if (passwordMode) viewModel.dispatch(AuthViewAction.LoginWithPassword)
                            else viewModel.dispatch(AuthViewAction.LoginWithToken)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF6777EF)
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            text = if (!registerMode) "登录" else "注册",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { registerMode = !registerMode },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF746E98)
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            text = if (!registerMode) "注册" else "返回登录",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { /*TODO 忘记密码*/ },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFA7DBCB)
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            text = "登录遇到问题?",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
    if (onLoading)
        WarpLoadingDialog()
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun AuthPagePreview() {
    AuthPage(null, null)
}