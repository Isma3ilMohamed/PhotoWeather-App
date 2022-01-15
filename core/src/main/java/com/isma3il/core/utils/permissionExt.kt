package com.isma3il.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import androidx.core.app.ActivityCompat
import androidx.core.os.BuildCompat


fun hasSdkHigherThan(sdk: Int): Boolean {
    //Early previous of R will return Build.VERSION.SDK_INT as 29
    if (Build.VERSION_CODES.R == sdk) {
        return VERSION.SDK_INT >= 30
    }
    return Build.VERSION.SDK_INT > sdk
}

fun Context.hasPermissions(permissions:Array<String>):Boolean{

    for (it in permissions){
        if (ActivityCompat.checkSelfPermission(this,it) != PackageManager.PERMISSION_GRANTED){
            return false

        }
    }
    return true
}