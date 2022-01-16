package com.isma3il.core.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.children

import io.reactivex.rxjava3.subjects.PublishSubject

object ScreenShotUtils {

    val screenShotSubject:PublishSubject<Bitmap> = PublishSubject.create()


    fun takeScreenShot(view: View,activity: Activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            takeScreenShotForHigherAPIs(view, activity)
        }else{
            drawChildrenView(view)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun takeScreenShotForHigherAPIs(view: View, activity: Activity){
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)
        val xCoordinate = locationOfViewInWindow[0]
        val yCoordinate = locationOfViewInWindow[1]

        val scope = Rect(
            xCoordinate,
            yCoordinate,
            xCoordinate + view.width,
            yCoordinate + view.height
        )

        val handlerThread = HandlerThread(PixelCopyUtils.PixelCopyListener::class.java.simpleName)
        handlerThread.start()

        PixelCopy.request(activity.window, scope, bitmap, { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
               screenShotSubject.onNext(bitmap)
            } else {
                screenShotSubject.onError(Exception("Can't take a screen shot"))
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper)
        )
    }

    private fun drawChildrenView(view: View):Bitmap? {

        val bitmap = Bitmap.createBitmap(view.width,view. height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        screenShotSubject.onNext(bitmap)
        return bitmap
    }

    private fun generateBitmap(v: ViewGroup){

            val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            v.draw(canvas)

            val children=v.children.toMutableList()


            for (element in children) {

                val childBitmap = drawChildrenView(element)
                if (childBitmap != null) {
                    canvas.drawBitmap(childBitmap, element.left.toFloat(), element.top.toFloat(), null)
                }
            }

        screenShotSubject.onNext(bitmap)
    }

}