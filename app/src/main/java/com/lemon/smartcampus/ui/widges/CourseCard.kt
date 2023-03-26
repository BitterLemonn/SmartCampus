package com.lemon.smartcampus.ui.widges

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemon.smartcampus.R
import com.lemon.smartcampus.data.database.entities.CourseEntity
import com.lemon.smartcampus.ui.theme.AppTheme

@Composable
fun CourseCard(
    courseEntity: CourseEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        backgroundColor = AppTheme.colors.card,
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "time",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = courseEntity.startTime,
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = courseEntity.endTime,
                    fontSize = 14.sp,
                    color = AppTheme.colors.textDarkColor
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TextWithIcon(icon = R.drawable.book, text = courseEntity.name)
                TextWithIcon(icon = R.drawable.location, text = courseEntity.location)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(
                        id = if (courseEntity.needAlarm) R.drawable.alarm_selected
                        else R.drawable.alarm
                    ),
                    contentDescription = "alarm"
                )
            }
        }
    }
}

@Composable
private fun TextWithIcon(
    @DrawableRes icon: Int,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, fontSize = 16.sp, color = AppTheme.colors.textDarkColor)
    }
}

@Composable
@Preview
private fun ClassCardPreview() {
    CourseCard(
        courseEntity = CourseEntity(
            startTime = "8:00",
            endTime = "9:30",
            name = "高等数学",
            location = "教学楼A202",
            needAlarm = true,
            alarmTime = 5,
            startWeek = 1,
            endWeek = 1,
            dayInWeek = "一",
            id = ""
        )
    )
}