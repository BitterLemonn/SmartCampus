package com.lemon.smartcampus.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.lemon.smartcampus.R

const val NOTIFICATION_ID_01 = 1

class NotificationUtils(
    private val context: Context,
    @DrawableRes val icon: Int,
    private val title: String,
    private val content: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val channel =
        NotificationChannel(SMART_CAMPUS, SMART_CAMPUS, NotificationManager.IMPORTANCE_DEFAULT)
    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationBuilder = if (Build.VERSION.SDK_INT > 26) Notification.Builder(
        context,
        SMART_CAMPUS
    ) else Notification.Builder(context)

    private fun show() {
        if (Build.VERSION.SDK_INT > 26) {
            channel.enableLights(false)
            channel.setShowBadge(false)
            manager.createNotificationChannel(channel)
        }
        manager.notify(NOTIFICATION_ID_01, notificationBuilder.build())
    }

    private fun build() {
        notificationBuilder
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
            .setSmallIcon(
                Icon.createWithBitmap(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.download2
                    )
                )
            )
            .setContentText(title)
            .setContentText(content)
            .setWhen(System.currentTimeMillis())
        show()
    }

    fun changeProgress(now: Int, total: Int) {
        notificationBuilder.setProgress(total, now, false)
        if (now >= total)
            manager.cancel(NOTIFICATION_ID_01)
        else build()
    }
}