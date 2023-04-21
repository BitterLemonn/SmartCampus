package com.lemon.smartcampus.ui.toolBtnPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@Composable
fun CalendarPage(
    onBack: () -> Unit
) {
    val pdfState = rememberVerticalPdfReaderState(
        ResourceType.Remote(
            url = "https://www.gdufs.edu.cn/__local/D/E0/1E/9E23BF8EA42E22104109975CD51_CE05120A_23B3E.pdf?e=.pdf"
        ), isZoomEnable = true
    )
    Column(Modifier.fillMaxSize()) {
        ColoredTitleBar(color = Color.Transparent, text = "学校校历") { onBack() }
        Spacer(modifier = Modifier.height(10.dp))
        VerticalPDFReader(state = pdfState, modifier = Modifier.fillMaxSize())
    }
}