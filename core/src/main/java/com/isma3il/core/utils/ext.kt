package com.isma3il.core.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson


//Gson Converting

inline fun <reified R> String.fromJson() : R {
    return Gson().fromJson(this, R::class.java)
}

inline fun <reified R> R.toJson() : String {
    return Gson().toJson(this, R::class.java)
}

//Load image
fun ImageView?.loadImage(url: String?) {
    this?.context?.let {
        Glide.with(it)
            .load(url)
            .into(this)
    }
}
fun ImageView?.loadImage(uri: Uri?) {
    this?.context?.let {
        Glide.with(it)
            .load(uri)
            .into(this)
    }
}

// constraint group click listener
fun androidx.constraintlayout.widget.Group.setAllOnClickListener(listener: View.OnClickListener?) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

