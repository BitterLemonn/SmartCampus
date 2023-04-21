package com.lemon.smartcampus.ui.widges

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.CharacterEntity
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun CharacterCard(entity: CharacterEntity, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp),
        backgroundColor = AppTheme.colors.card
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp))
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
        ) {
            Row(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(entity.imgUrl).build(),
                    contentDescription = "${entity.name}'s image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme())
                        ColorFilter.tint(Color.Gray, BlendMode.Multiply) else null
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = entity.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.colors.textBlackColor
                    )
                    Text(
                        text = entity.introduction,
                        fontSize = 14.sp,
                        color = AppTheme.colors.textDarkColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.right_half),
                            contentDescription = "right half",
                            modifier = Modifier.size(40.dp),
                            colorFilter = if (isSystemInDarkTheme())
                                ColorFilter.tint(Color.Gray) else null
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .width(8.dp)
                        .background(AppTheme.colors.schoolBlue)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CharacterCardPreview() {
    CharacterCard(
        entity = CharacterEntity(
            imgUrl = "https://www.gdufs.edu.cn/__local/A/B6/5B/C6975FD87106CD2AF42774EEB94_60C4D60B_1145D.jpg",
            name = "梁宗岱",
            introduction = "梁宗岱(1903～1983)，是中国现代文学史上一位集诗人、理论家、批评家、翻译家、教育家于一身的罕见人才。16岁成为饮誉文坛的“南国诗人”，精通英法德意等多门外语，结缘世界文学大师保尔•瓦雷里和罗曼•罗兰，相交文化名人徐志摩、朱光潜、巴金、胡适等，谱写了中外文化交流史上的一段佳话;一生跌宕传奇，曾在北大、南开、复旦、中大等多所高校执教，后随院系调整到广外，任教法语。广东外语外贸大学是他生前长期执教的最后一所高校，在广外度过人生的最后13年，留给广外后人无可计数的财富。"
        )
    ) {}
}