package com.lemon.smartcampus.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.lemon.smartcampus.data.database.database.GlobalDataBase
import com.lemon.smartcampus.data.globalData.AppContext
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.roundToInt


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

fun uri2Path(context: Context, uri: Uri): String? {
    if (ContentResolver.SCHEME_FILE == uri.scheme) {
        return uri.path
    } else if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val authority = uri.authority
        var idStr = ""
        if (authority != null) {
            Logger.d("authority: $authority")
            if (authority == "media") {
                idStr = uri.toString().substring(uri.toString().lastIndexOf('/') + 1)
            } else if (authority.startsWith("com.android.providers")) {
                idStr = DocumentsContract.getDocumentId(uri).split(":".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            }
            val contentResolver = context.contentResolver
            var cursor = contentResolver.query(
                uri,
                arrayOf(MediaStore.Files.FileColumns.DATA),
                null,
                arrayOf(idStr),
                null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                try {
                    var idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                    Logger.d("path: ${cursor.getString(idx)}")

                    if (cursor.getString(idx) == null
                        && uri.scheme.equals(ContentResolver.SCHEME_CONTENT)
                        && Build.VERSION.SDK_INT > 29
                    ) {
                        var displayName = "${System.currentTimeMillis()}" +
                                "${((Math.random() + 1) * 1000).roundToInt()}." +
                                "${MimeTypeMap.getSingleton()
                                        .getExtensionFromMimeType(contentResolver.getType(uri))}"
                        cursor = contentResolver.query(
                            uri,
                            arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME),
                            null,
                            arrayOf(idStr),
                            null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                            displayName = cursor.getString(idx) ?: displayName
                        }
                        //把文件复制到沙盒目录
                        val inputStream = contentResolver.openInputStream(uri)
                        val cache = File(context.cacheDir.absolutePath, displayName)
                        val fos = FileOutputStream(cache)
                        inputStream?.let { FileUtils.copy(it, fos) }
                        val path = cache.path
                        fos.close()
                        inputStream?.close()
                        Logger.d("path2: $path")
                        return path
                    } else return cursor.getString(idx)
                } catch (_: Exception) {
                } finally {
                    cursor?.close()
                }
            }
        }
    }
    return null
}

fun saveBitmapToFile(file: File): File? {
    return try {

        // BitmapFactory options to downsize the image
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        // factor of downsizing the image
        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        // The new size we want to scale to
        val REQUIRED_SIZE = 75

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
            o.outHeight / scale / 2 >= REQUIRED_SIZE
        ) {
            scale *= 2
        }
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()

        // here i override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        file
    } catch (e: java.lang.Exception) {
        null
    }
}

fun logoutLocal(scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
        AppContext.profile = null
        GlobalDataBase.database.profileDao().deleteAll()
    }
}

