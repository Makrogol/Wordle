package com.serebryakov.wordle.foundation.tools

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


object ScreenUtils {

    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels
    }
}