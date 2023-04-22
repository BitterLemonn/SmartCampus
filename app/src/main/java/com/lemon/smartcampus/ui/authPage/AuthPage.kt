package com.lemon.smartcampus.ui.authPage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.AuthOutlinedTextField
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.WarpLoadingDialog
import com.lemon.smartcampus.viewModel.auth.AuthViewAction
import com.lemon.smartcampus.viewModel.auth.AuthViewEvent
import com.lemon.smartcampus.viewModel.auth.AuthViewModel
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.delay

@Composable
fun AuthPage(
    showToast: (String, String) -> Unit,
    viewModel: AuthViewModel = viewModel(),
    onBack: () -> Unit
) {
    val viewStates = viewModel.viewStates
    val state by viewStates.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    var showLogo by remember { mutableStateOf(true) }
    var showSettingTimes by remember { mutableStateOf(10) }
    var showSetting by remember { mutableStateOf(false) }
    var publicUrl by remember { mutableStateOf(AppContext.publicUrl) }

    var checkPassword by remember { mutableStateOf("") }

    var needWait by rememberSaveable { mutableStateOf(0) }
    var onLoading by remember { mutableStateOf(false) }

    var passwordMode by remember { mutableStateOf(true) }
    var registerMode by remember { mutableStateOf(false) }
    var helpMode by remember { mutableStateOf(false) }
    var changeMode by remember { mutableStateOf(false) }

    fun checkChangePassword() {
        if (checkPassword == state.password) viewModel.dispatch(AuthViewAction.ChangePassword)
        else showToast("请检查两次密码一致性", SNACK_ERROR)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is AuthViewEvent.TransIntent -> onBack.invoke()
                is AuthViewEvent.ShowToast -> showToast(it.msg, SNACK_ERROR)
                is AuthViewEvent.AlreadyRequestCode -> needWait = 120
                is AuthViewEvent.ShowLoadingDialog -> onLoading = true
                is AuthViewEvent.DismissLoadingDialog -> onLoading = false
                is AuthViewEvent.CheckSuccess -> {
                    changeMode = true
                    helpMode = false
                }
                is AuthViewEvent.ChangeSuccess -> {
                    changeMode = false
                    helpMode = false
                }
            }
        }
    }
    LaunchedEffect(key1 = needWait) {
        if (needWait > 0) {
            delay(1_000)
            needWait -= 1
        }
    }

    val animateTokenSize by animateFloatAsState(targetValue = if (passwordMode) 1f else 0.4f)
    val animateGetWidth by animateDpAsState(targetValue = if (passwordMode) 0.dp else Dp.Unspecified)
    val animateRegHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else Dp.Unspecified)
    val animateSpacerHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else 20.dp)

    val animateHelpHeight by animateDpAsState(
        targetValue = if (helpMode || changeMode) 460.dp else 0.dp, tween(400)
    )
    val animateAuthHeight by animateDpAsState(
        targetValue = if (helpMode || changeMode) 0.dp else 460.dp, tween(400)
    )
    val animateHelpWidth by animateFloatAsState(
        targetValue = if (changeMode && !helpMode) 0f else 1f, tween(400)
    )
    val animateChangeWidth by animateFloatAsState(
        targetValue = if (changeMode && !helpMode) 1f else 0f, tween(400)
    )

    LaunchedEffect(key1 = passwordMode) {
        viewModel.dispatch(AuthViewAction.UpdateToken(""))
        viewModel.dispatch(AuthViewAction.UpdatePassword(""))
    }

    LaunchedEffect(key1 = registerMode) {
        passwordMode = !registerMode
        viewModel.dispatch(AuthViewAction.UpdatePassword(""))
    }

    if (showLogo) Box(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(indication = rememberRipple(),
                    interactionSource = MutableInteractionSource(),
                    onClick = { onBack.invoke() })
                .padding(5.dp)
                .size(20.dp)
        )
    }
    BoxWithConstraints {
        showLogo = maxHeight > 600.dp
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showLogo) {
                Image(painter = painterResource(id = R.drawable.school_logo),
                    contentDescription = "school logo",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clickable(
                            indication = null, interactionSource = MutableInteractionSource()
                        ) {
                            showSettingTimes -= 1
                            if (showSettingTimes <= 0) {
                                showSettingTimes = 1
                                showSetting = true
                            }
                        })
                Spacer(modifier = Modifier.height(20.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight(),
                elevation = 10.dp,
                shape = RoundedCornerShape(5.dp),
                backgroundColor = AppTheme.colors.card
            ) {
                Column(Modifier.height(460.dp)) {
                    // 登录注册
                    Column(modifier = Modifier.height(animateAuthHeight)) {
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
                            AuthOutlinedTextField(value = state.phone,
                                onValueChange = {
                                    if (it.length <= 11) viewModel.dispatch(
                                        AuthViewAction.UpdatePhone(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(if (!helpMode && !changeMode) 1f else 0f),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                                ),
                                label = { Text(text = "手机号", fontSize = 14.sp) })
                            Spacer(modifier = Modifier.height(20.dp))

                            // 注册密码
                            AuthOutlinedTextField(
                                value = state.password,
                                onValueChange = {
                                    if (it.length <= 50) viewModel.dispatch(
                                        AuthViewAction.UpdatePassword(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(animateRegHeight)
                                    .alpha(if (registerMode && !helpMode) 1f else 0f),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                                ),
                                visualTransformation = PasswordVisualTransformation(),
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
                                AuthOutlinedTextField(value = if (!passwordMode) state.token else state.password,
                                    onValueChange = {
                                        if (!passwordMode) {
                                            if (it.length <= 4) viewModel.dispatch(
                                                AuthViewAction.UpdateToken(
                                                    it
                                                )
                                            )
                                        } else {
                                            if (it.length <= 50) viewModel.dispatch(
                                                AuthViewAction.UpdatePassword(
                                                    it
                                                )
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(animateTokenSize)
                                        .alpha(if (!helpMode && !changeMode) 1f else 0f),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = if (passwordMode) KeyboardType.Password
                                        else KeyboardType.Number, imeAction = ImeAction.Go
                                    ),
                                    keyboardActions = KeyboardActions(onGo = {
                                        if (registerMode) viewModel.dispatch(AuthViewAction.Register)
                                        else if (passwordMode) viewModel.dispatch(AuthViewAction.LoginWithPassword)
                                        else viewModel.dispatch(AuthViewAction.LoginWithToken)
                                    }),
                                    visualTransformation = if (passwordMode) PasswordVisualTransformation()
                                    else VisualTransformation.None,
                                    label = {
                                        Text(
                                            text = if (passwordMode) "密码" else "验证码",
                                            fontSize = 14.sp
                                        )
                                    })
                                Button(
                                    onClick = { viewModel.dispatch(AuthViewAction.RequestToken) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        1.dp, color = AppTheme.colors.textLightColor
                                    ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                    modifier = Modifier
                                        .width(animateGetWidth)
                                        .widthIn(min = 120.dp),
                                    enabled = needWait == 0
                                ) {
                                    Text(
                                        text = if (needWait == 0) "获取验证码" else "${needWait}s",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp, vertical = 5.dp
                                        ),
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
                                onClick = {
                                    viewModel.dispatch(AuthViewAction.UpdatePhone(""))
                                    viewModel.dispatch(AuthViewAction.UpdateToken(""))
                                    helpMode = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFFA7DBCB)
                                ),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 5.dp
                                ),
                                enabled = !registerMode
                            ) {
                                Text(
                                    text = "登录遇到问题?",
                                    color = Color.White,
                                    modifier = Modifier.padding(vertical = 5.dp)
                                )
                            }
                        }
                    }
                    // 忘记密码
                    Column(modifier = Modifier.height(animateHelpHeight)) {
                        Divider(
                            color = AppTheme.colors.schoolRed,
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp
                        )
                        if (helpMode || changeMode) Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "忘记密码",
                                color = if (!isSystemInDarkTheme()) AppTheme.colors.schoolBlue
                                else Color(0xFFEAEAEA),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(Modifier.fillMaxWidth()) {
                                // 验证界面
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(animateHelpWidth)
                                        .fillMaxHeight()
                                ) {
                                    // 手机号
                                    AuthOutlinedTextField(value = state.phone,
                                        onValueChange = {
                                            if (it.length <= 11) viewModel.dispatch(
                                                AuthViewAction.UpdatePhone(
                                                    it
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .alpha(if (helpMode) 1f else 0f),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
                                            imeAction = ImeAction.Next
                                        ),
                                        label = { Text(text = "手机号", fontSize = 14.sp) })
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // 验证码
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AuthOutlinedTextField(value = state.token,
                                            onValueChange = {
                                                if (it.length <= 4) viewModel.dispatch(
                                                    AuthViewAction.UpdateToken(
                                                        it
                                                    )
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth(0.4f)
                                                .alpha(if (helpMode) 1f else 0f),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Go
                                            ),
                                            keyboardActions = KeyboardActions(onGo = {
                                                viewModel.dispatch(AuthViewAction.CheckForgetCode)
                                            }),
                                            label = { Text(text = "验证码", fontSize = 14.sp) })
                                        Button(
                                            onClick = {
                                                viewModel.dispatch(AuthViewAction.RequestToken)
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(
                                                1.dp, color = AppTheme.colors.textLightColor
                                            ),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                            modifier = Modifier.widthIn(min = 120.dp),
                                            enabled = needWait == 0
                                        ) {
                                            Text(
                                                text = if (needWait == 0) "获取验证码" else "${needWait}s",
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 10.dp, vertical = 5.dp
                                                ),
                                                maxLines = 1,
                                                color = AppTheme.colors.textLightColor
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.SpaceAround,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Button(
                                            onClick = {
                                                viewModel.dispatch(AuthViewAction.CheckForgetCode)
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
                                                text = "下一步",
                                                color = Color.White,
                                                modifier = Modifier.padding(vertical = 5.dp),
                                                maxLines = 1
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                viewModel.dispatch(AuthViewAction.UpdatePhone(""))
                                                viewModel.dispatch(AuthViewAction.UpdateToken(""))
                                                helpMode = false
                                            },
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
                                                text = "返回登录",
                                                color = Color.White,
                                                modifier = Modifier.padding(vertical = 5.dp),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                                // 修改界面
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(animateChangeWidth)
                                        .fillMaxHeight()
                                ) {
                                    AuthOutlinedTextField(
                                        value = state.password,
                                        onValueChange = {
                                            if (it.length < 50) viewModel.dispatch(
                                                AuthViewAction.UpdatePassword(
                                                    it
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .alpha(if (changeMode) 1f else 0f),
                                        label = { Text(text = "密码", fontSize = 14.sp) },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Next
                                        ),
                                        visualTransformation = PasswordVisualTransformation()
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    AuthOutlinedTextField(
                                        value = checkPassword,
                                        onValueChange = { checkPassword = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .alpha(if (changeMode) 1f else 0f),
                                        label = { Text(text = "确认密码", fontSize = 14.sp) },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Go
                                        ),
                                        keyboardActions = KeyboardActions(onGo = { checkChangePassword() }),
                                        visualTransformation = PasswordVisualTransformation(),
                                        isError = checkPassword != state.password
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(
                                        onClick = { checkChangePassword() },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(0xFF6777EF)
                                        ),
                                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                                    ) {
                                        Text(
                                            text = "完成修改",
                                            color = Color.White,
                                            modifier = Modifier.padding(vertical = 5.dp),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (onLoading) WarpLoadingDialog()
    if (showSetting) Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = MutableInteractionSource(), indication = null
        ) {}
        .background(Color(0x88000000)), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.8f),
            elevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = AppTheme.colors.card
        ) {
            Column(Modifier.fillMaxWidth()) {
                TextField(
                    value = publicUrl,
                    onValueChange = { publicUrl = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "输入公网地址,留空以使用内网",
                            fontSize = 14.sp,
                            color = AppTheme.colors.textDarkColor
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = AppTheme.colors.schoolBlue
                    ),
                    textStyle = TextStyle(fontSize = 14.sp, color = AppTheme.colors.textBlackColor)
                )
                Button(
                    onClick = {
                        if (publicUrl.matches("^http.*?/$".toRegex()) || publicUrl.isEmpty()) {
                            showSetting = false
                            AppContext.publicUrl = publicUrl
                        } else showToast("连接必须以http(s)开头/结尾", SNACK_ERROR)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.schoolBlue)
                ) {
                    Text(text = "确定", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun AuthPagePreview() {
    AuthPage({ _, _ -> }) {}
}