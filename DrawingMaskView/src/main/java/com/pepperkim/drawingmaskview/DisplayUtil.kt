package com.pepperkim.drawingmaskview

import android.content.Context
import kotlin.math.roundToInt

object DisplayUtil {

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun pxToDp(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).roundToInt()
    }
}