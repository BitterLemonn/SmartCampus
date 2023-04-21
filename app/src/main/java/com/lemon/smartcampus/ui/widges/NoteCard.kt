package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.data.database.entities.NoteEntity
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    noteEntity: NoteEntity,
    onCLick: () -> Unit
) {
    Card(
        modifier = Modifier
            .widthIn(min = 150.dp)
            .then(modifier),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = AppTheme.colors.card
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCLick
                )
        ) {
            Column(Modifier.padding(10.dp)) {
                Text(
                    text = noteEntity.title,
                    color = AppTheme.colors.textBlackColor,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = noteEntity.content,
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 10,
                    modifier = Modifier.widthIn(min = 50.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = noteEntity.createTime.split(" ").first(),
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@Preview
private fun NoteCardPreview() {
    NoteCard(
        noteEntity = NoteEntity(
            "标题", "内容内容内容内容内容内容内容内容内容内容内容内容内容" +
                    "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容",
            "1980年01月01日"
        )
    ) {}
}