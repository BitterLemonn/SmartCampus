package com.lemon.smartcampus.ui.discoverPage.tabPage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lemon.smartcampus.data.database.entities.CommentEntity
import com.lemon.smartcampus.data.database.entities.TopicEntity
import com.lemon.smartcampus.data.globalData.AppContext
import com.lemon.smartcampus.ui.widges.*

@Composable
fun TopicDetailPage(navController: NavController?) {
    var key by remember { mutableStateOf("") }
    var isTouchOutSide by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }

    var host: TopicEntity = remember { TopicEntity.getEmpty().copy() }
    val commentList = remember { mutableStateListOf<CommentEntity>() }
    Column(
        Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = {
                    isTouchOutSide = true
                    isShow = false
                    Log.d("isTouchOutSide", "1")
                })
    ) {
        ToolBarMore(onBack = { navController?.popBackStack() }, onMore = {
            isShow = true
        })

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            item { CommentHostCard(topic = host) }
            if (host.nickName.isNotBlank()) items(commentList) {
                CommentSubCard(comment = it)
            }
        }

        ChatBox(key, isTouchOutside = isTouchOutSide, onChange = { key = it }, onSend = {

        }, onTouch = { isTouchOutSide = false })
    }

    if (host.userID == AppContext.profile?.id && host.userID.isNotBlank())
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            MoreActionCard(listOf(ActionPair("删除话题") {
                // TODO 删除
            }), isShow)
        }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
private fun ResDetailPagePreview() {
    TopicDetailPage(navController = null)
}