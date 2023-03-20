package com.lemon.smartcampus.ui.authPage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import com.lemon.smartcampus.R
import com.lemon.smartcampus.ui.theme.SchoolBlueDay
import com.lemon.smartcampus.ui.theme.TextLightDay

@Composable
fun AuthPage(navController: NavController?) {
    var phoneNum by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var check by remember { mutableStateOf("") }

    var passwordMode by remember { mutableStateOf(true) }
    var registerMode by remember { mutableStateOf(false) }

    val animateTokenSize by animateFloatAsState(targetValue = if (passwordMode) 1f else 0.4f)
    val animateGetWidth by animateDpAsState(targetValue = if (passwordMode) 0.dp else Dp.Unspecified)
    val animateRegHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else Dp.Unspecified)
    val animateSpacerHeight by animateDpAsState(targetValue = if (!registerMode) 0.dp else 20.dp)

    LaunchedEffect(key1 = passwordMode) {
        token = ""
    }

    LaunchedEffect(key1 = registerMode) {
        passwordMode = !registerMode
        check = ""
    }

    Box(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        Image(painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(
                    CircleShape
                )
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
            shape = RoundedCornerShape(5.dp)
        ) {
            Column {
                Divider(color = SchoolBlueDay, modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
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
                            color = SchoolBlueDay,
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
                    OutlinedTextField(value = phoneNum,
                        onValueChange = { if (it.length <= 11) phoneNum = it },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = SchoolBlueDay,
                            unfocusedBorderColor = TextLightDay,
                            focusedBorderColor = SchoolBlueDay,
                            focusedLabelColor = SchoolBlueDay
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        textStyle = TextStyle(fontSize = 16.sp, lineHeight = 14.sp),
                        label = { Text(text = "手机号", fontSize = 14.sp) })
                    Spacer(modifier = Modifier.height(20.dp))

                    // 注册密码
                    OutlinedTextField(
                        value = check,
                        onValueChange = { if (check.length < 50) check = it },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = SchoolBlueDay,
                            unfocusedBorderColor = TextLightDay,
                            focusedBorderColor = SchoolBlueDay,
                            focusedLabelColor = SchoolBlueDay,
                            disabledBorderColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animateRegHeight),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(fontSize = 16.sp, lineHeight = 14.sp),
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
                        OutlinedTextField(value = token,
                            onValueChange = {
                                if (!passwordMode) {
                                    if (token.length < 4) token = it
                                } else {
                                    if (token.length < 50) token = it
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = SchoolBlueDay,
                                unfocusedBorderColor = TextLightDay,
                                focusedBorderColor = SchoolBlueDay,
                                focusedLabelColor = SchoolBlueDay
                            ),
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.fillMaxWidth(animateTokenSize),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                            ),
                            visualTransformation = if (passwordMode) PasswordVisualTransformation()
                            else VisualTransformation.None,
                            textStyle = TextStyle(fontSize = 16.sp, lineHeight = 14.sp),
                            singleLine = true,
                            label = {
                                Text(text = if (passwordMode) "密码" else "验证码", fontSize = 14.sp)
                            })
                        Button(
                            onClick = { /*TODO 获取验证码*/ },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, color = TextLightDay),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            modifier = Modifier.width(animateGetWidth)
                        ) {
                            Text(
                                text = "获取验证码",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                maxLines = 1,
                                color = TextLightDay
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { /*TODO 登录*/ },
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
                        onClick = { /*TODO 登录*/ },
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
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun AuthPagePreview() {
    AuthPage(null)
}