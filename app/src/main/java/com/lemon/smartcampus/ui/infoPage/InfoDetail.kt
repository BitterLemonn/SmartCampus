package com.lemon.smartcampus.ui.infoPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.orhanobut.logger.Logger
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InfoDetail(
    navController: NavController?,
    scaffoldState: ScaffoldState?,
    infoID: String
) {
    val textList = remember { mutableStateListOf<String>() }
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        this.launch(Dispatchers.IO) {
            val news = GlobalDataBase.database.infoDao().getNewsById(infoID)
            val text = if (news == null) {
                val academic = GlobalDataBase.database.infoDao().getAcademicById(infoID)
                title = academic?.informationTitle ?: ""
                time = academic?.publishDate?.split(" ")?.first() ?: ""
                (academic?.informationContent ?: "").replace("\t", "　　")

            } else {
                title = news.informationTitle
                time = news.publishDate.split(" ").first()
                news.informationContent.replace("\t", "　　")
            }
            textList.addAll(text.split("\n"))
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ColoredTitleBar(color = Color.Transparent, text = "") {
            navController?.popBackStack()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = AppTheme.colors.textBlackColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = time, fontSize = 14.sp, color = AppTheme.colors.textDarkColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(textList) { text ->
                Logger.d("text: $text")
                if (text.matches("^　　<center.*?center>$".toRegex())) {
                    if (text.contains("http")) {
                        var imgUrl = "src=\".*?\"".toRegex().find(text)?.value
                        if (!imgUrl.isNullOrBlank()) {
                            imgUrl = imgUrl.split("\"")[1]
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = imgUrl,
                                    contentDescription = "news image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .heightIn(200.dp)
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text.split("**")[1],
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textDarkColor
                            )
                        }
                    }
                } else
                    MarkdownText(
                        markdown = text,
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colors.textDarkColor,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        style = TextStyle(lineHeight = 8.sp)
                    )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
fun InfoDetailPreview() {
    InfoDetail(navController = null, scaffoldState = null, infoID = "")
}