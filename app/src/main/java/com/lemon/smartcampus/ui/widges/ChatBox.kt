package com.lemon.smartcampus.ui.widges

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.entities.ProfileEntity
import com.lemon.smartcampus.data.globalData.ProfileInfo
import com.lemon.smartcampus.ui.theme.SchoolBlueDay
import com.lemon.smartcampus.ui.theme.TextDarkDay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatBox(
    key: String,
    isTouchOutside: Boolean = false,
    onTouch: () -> Unit,
    onChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    val profile: ProfileEntity = ProfileInfo.profile!!
    val rememberKey by rememberUpdatedState(newValue = key)

    var typeMode by remember { mutableStateOf(false) }
    val isTouchRemember by rememberUpdatedState(newValue = isTouchOutside)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = typeMode) {
        Log.d("typeMode", "$typeMode")
        if (typeMode && key.isEmpty()) {
            keyboardController?.show()
            focusRequester.requestFocus()
        } else keyboardController?.hide()
        if (key.isNotEmpty())
            typeMode = true
    }

    LaunchedEffect(isTouchRemember) {
        Log.d("launch", "get: $isTouchOutside")
        if (isTouchOutside) {
            typeMode = false
            onTouch.invoke()
        }
    }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA)),
        verticalAlignment = Alignment.Bottom
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            elevation = 10.dp,
            backgroundColor = Color.White
        ) {
            if (profile.iconUrl.isNotBlank()) AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(profile.iconUrl),
                contentDescription = "icon",
                modifier = Modifier.fillMaxWidth()
            )
            else Image(
                painter = painterResource(id = R.drawable.cat_logo),
                contentDescription = "icon",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp, max = 180.dp),
            backgroundColor = Color.White
        ) {
            Box(
                modifier = Modifier.animateContentSize(animationSpec = tween()),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    horizontalArrangement = if (typeMode || key.isNotBlank()) Arrangement.SpaceBetween else Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (typeMode || key.isNotBlank()) Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 10.dp)
                            .verticalScroll(
                                state = scrollState,
                                flingBehavior = ScrollableDefaults.flingBehavior()
                            )
                    ) {
                        TextField(
                            value = rememberKey,
                            onValueChange = {
                                onChange.invoke(it)
                                typeMode = true
                                scope.launch { scrollState.scrollTo(Int.MAX_VALUE) }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = TextStyle(color = TextDarkDay, fontSize = 18.sp),
                            keyboardActions = KeyboardActions(onSend = {
                                onSend.invoke(rememberKey)
                            }),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = TextDarkDay,
                                cursorColor = SchoolBlueDay,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                backgroundColor = Color.White
                            )
                        )
                    } else Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 10.dp)
                            .clickable(interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(),
                                onClick = { typeMode = true })
                    )
                    Box(
                        modifier = Modifier.padding(
                            bottom = if (typeMode || key.isNotBlank()) 10.dp else 0.dp
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.send),
                            contentDescription = "send",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable(interactionSource = MutableInteractionSource(),
                                    indication = rememberRipple(),
                                    onClick = { onSend.invoke(key) })
                                .padding(5.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 注意: ChatBoxFull是一个占满全屏的控件,请将它直接放于根布局
 */
@Composable
fun ChatBoxFull(
    onSend: (String) -> Unit
) {
    var key by remember { mutableStateOf("") }
    var isTouchOutSide by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = {
                    isTouchOutSide = true
                    Log.d("isTouchOutSide", "1")
                }), contentAlignment = Alignment.BottomCenter
    ) {
        ChatBox(key,
            isTouchOutside = isTouchOutSide,
            onChange = { key = it },
            onSend = onSend,
            onTouch = { isTouchOutSide = false })
    }
}

@Composable
@Preview
private fun ChatBoxPreview() {
    ChatBox(key = "", onTouch = {}, onChange = {}, onSend = {})
}