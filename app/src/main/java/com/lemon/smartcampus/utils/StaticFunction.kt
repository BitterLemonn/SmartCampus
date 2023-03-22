package com.lemon.smartcampus.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.orhanobut.logger.Logger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


fun md5(string: String): String {
    if (string.isEmpty()) {
        return ""
    }
    val md5: MessageDigest?
    try {
        md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(string.toByteArray())
        val result = StringBuilder()
        for (b in bytes) {
            var temp = Integer.toHexString(b.toInt() and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result.append(temp)
        }
        println(result.toString())
        return result.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

fun imageUri2Path(context: Context, uri: Uri): String? {
    if (ContentResolver.SCHEME_FILE == uri.scheme) {
        return uri.path
    } else if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val authority = uri.authority
        if (authority == "media"){
            val idStr = uri.toString().substring(uri.toString().lastIndexOf('/') + 1)
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                "_id=?",
                arrayOf(idStr),
                null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                try {
                    val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    return cursor.getString(idx)
                } catch (_: Exception) {
                } finally {
                    cursor.close()
                }
            }
        }
    }
    return null
}
