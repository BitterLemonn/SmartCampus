package com.lemon.smartcampus.ui.widges

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun ProfileEditCard(
    isShow: MutableState<Boolean>,
    nickname: String,
    tags: List<String>,
    onChange: (String, String) -> Unit = { _, _ -> }
) {
    var nicknameRemember by remember { mutableStateOf(nickname) }
    var tagString by remember { mutableStateOf("") }

    fun tagReset() {
        tagString = ""
        tags.forEach { tagString += (if (tags.last() != it) "$it " else it) }
    }

    LaunchedEffect(key1 = Unit) { tagReset() }

    if (isShow.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) {}
        )
    AnimatedVisibility(
        visible = isShow.value,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(modifier = Modifier.height(300.dp)) {
            Card(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = AppTheme.colors.card,
                shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = nicknameRemember,
                        onValueChange = { if (it.length <= 10) nicknameRemember = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        textStyle = TextStyle(
                            color = AppTheme.colors.textBlackColor,
                            fontSize = 16.sp
                        ),
                        placeholder = {},
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = if (!isSystemInDarkTheme()) Color.White
                            else Color(0xFF2F2F2F),
                            cursorColor = AppTheme.colors.schoolBlue,
                            unfocusedBorderColor = AppTheme.colors.textLightColor,
                            focusedBorderColor = AppTheme.colors.schoolBlue,
                            focusedLabelColor = AppTheme.colors.schoolBlue,
                            unfocusedLabelColor = AppTheme.colors.textBlackColor
                        ),
                        label = { Text(text = "昵称", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        maxLines = 1,
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = tagString,
                        onValueChange = { if (it.length <= 10) tagString = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        textStyle = TextStyle(
                            color = AppTheme.colors.textBlackColor,
                            fontSize = 16.sp
                        ),
                        placeholder = {
                            Text(text = "输入你的标签吧,使用空格分开,最多两个标签", fontSize = 14.sp)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = if (!isSystemInDarkTheme()) Color.White
                            else Color(0xFF2F2F2F),
                            cursorColor = AppTheme.colors.schoolBlue,
                            unfocusedBorderColor = AppTheme.colors.textLightColor,
                            focusedBorderColor = AppTheme.colors.schoolBlue,
                            focusedLabelColor = AppTheme.colors.schoolBlue,
                            unfocusedLabelColor = AppTheme.colors.textBlackColor,
                            placeholderColor = AppTheme.colors.textLightColor
                        ),
                        label = { Text(text = "标签", fontSize = 14.sp) },
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                isShow.value = false
                                tagReset()
                                nicknameRemember = nickname
                            },
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 40.dp),
                            elevation = ButtonDefaults.elevation(5.dp, 10.dp, 0.dp, 0.dp, 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.card)
                        ) {
                            Text(
                                text = "取消",
                                fontSize = 14.sp,
                                color = AppTheme.colors.textBlackColor
                            )
                        }
                        Button(
                            onClick = {
                                onChange.invoke(nicknameRemember, tagString)
                                tagReset()
                                nicknameRemember = nickname
                                isShow.value = false
                            },
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 40.dp),
                            elevation = ButtonDefaults.elevation(5.dp, 10.dp, 0.dp, 0.dp, 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.card)
                        ) {
                            Text(text = "确定", fontSize = 14.sp, color = AppTheme.colors.schoolBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun ProfileEditCardPreview() {
    ProfileEditCard(
        isShow = remember { mutableStateOf(true) },
        nickname = "Lemon",
        tags = listOf("软工1904", "2019级")
    )
}