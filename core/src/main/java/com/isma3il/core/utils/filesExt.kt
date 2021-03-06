package com.isma3il.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

import androidx.core.view.children
import androidx.core.view.drawToBitmap


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): Pair<File, String> {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var storagePath = ""
    val savedFile = File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).also {
        storagePath = it.absolutePath
    }

    return Pair(savedFile, storagePath)
}


fun Context.saveImage(uri: Uri, bitmap: Bitmap): @NonNull Observable<Boolean> {
    return Observable.fromCallable {
       return@fromCallable contentResolver.openOutputStream(uri, "w").use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
