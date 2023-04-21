package com.lemon.smartcampus.ui.toolBtnPage.characterPage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.data.database.entities.CharacterEntity
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.CharacterDetailCard

@Composable
fun CharacterDetailPage(
    name: String,
    content: String,
    imgUrl: String
) {
    val verticalState = remember { ScrollState(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalState)
    ) {
        CharacterDetailCard(
            entity = CharacterEntity(
                name = name,
                imgUrl = imgUrl,
                introduction = content
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colors.textBlackColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = content,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            textAlign = TextAlign.Justify,
            color = AppTheme.colors.textDarkColor
        )
    }
}

@Composable
@Preview
private fun CharacterDetailPagePreview() {
    CharacterDetailPage("", "", "")
}