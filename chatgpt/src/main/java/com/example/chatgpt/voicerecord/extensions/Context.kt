package com.example.chatgpt.voicerecord.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Environment
import com.example.chatgpt.R
import com.example.chatgpt.voicerecord.helpers.Config
import com.simplemobiletools.commons.extensions.internalStoragePath
import com.simplemobiletools.commons.helpers.isQPlus

val Context.config: Config get() = Config.newInstance(applicationContext)

fun Context.drawableToBitmap(drawable: Drawable): Bitmap {
    val size = (60 * resources.displayMetrics.density).toInt()
    val mutableBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(mutableBitmap)
    drawable.setBounds(0, 0, size, size)
    drawable.draw(canvas)
    return mutableBitmap
}

fun Context.getDefaultRecordingsFolder(): String {
    val defaultPath = getDefaultRecordingsRelativePath()
    return "$internalStoragePath/$defaultPath"
}

fun Context.getDefaultRecordingsRelativePath(): String {
    return if (isQPlus()) {
        "${Environment.DIRECTORY_MUSIC}/LanguageAILearning"
    } else {
        getString(R.string.app_name)
    }
}
