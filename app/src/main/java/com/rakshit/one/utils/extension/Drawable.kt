package com.test.papers.utils.extension

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt

/**
 * setSolid
 * setSolidRadius
 * setStroke
 * setGradient
 * */

/// todo change base implementation

fun View.setSolid(shape: Int = GradientDrawable.RECTANGLE, @ColorInt color: Int = Color.parseColor("#FFFFFFFF")) {
    val drawable = GradientDrawable().apply {
        this@apply.shape = shape
        setColor(color)
    }
    background = drawable
}


fun View.setSolidRadius(
    shape: Int = GradientDrawable.RECTANGLE,
    @ColorInt color: Int = Color.parseColor("#FFFFFFFF"), leftTop: Int = 0,
    leftBottom: Int = 0,
    rightTop: Int = 0,
    rightBottom: Int = 0
) {
    val drawable = GradientDrawable().apply {
        this@apply.shape = shape
        setColor(color)
        cornerRadii = floatArrayOf(
            dpToPx(leftTop).toFloat(),
            dpToPx(leftTop).toFloat(),
            dpToPx(rightTop).toFloat(),
            dpToPx(rightTop).toFloat(),
            dpToPx(rightBottom).toFloat(),
            dpToPx(rightBottom).toFloat(),
            dpToPx(leftBottom).toFloat(),
            dpToPx(leftBottom).toFloat()
        )

    }
    background = drawable
}

fun View.setSolidRadius(
    shape: Int = GradientDrawable.RECTANGLE,
    @ColorInt color: Int = Color.parseColor("#FFFFFFFF"), radius: Int = 0
) {
    val drawable = GradientDrawable().apply {
        this@apply.shape = shape
        setColor(color)
        cornerRadius = dpToPx(radius).toFloat()

    }
    background = drawable
}

fun View.setStroke(
    shape: Int = GradientDrawable.RECTANGLE,
    width: Int,
    @ColorInt color: Int = Color.parseColor("#FFFFFFFF"),
    @ColorInt strokeColor: Int = Color.parseColor("#FFFFFFFF"),
    dashWidth: Int = 0,
    dashGap: Int = 0,
    radius: Int = 0
) {
    val drawable = GradientDrawable().apply {
        this@apply.shape = shape
        setColor(color)
        setStroke(dpToPx(width), strokeColor, dpToPx(dashWidth).toFloat(), dpToPx(dashGap).toFloat())
        cornerRadius = dpToPx(radius).toFloat()
    }
    background = drawable
}

fun View.setGradient(
    shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray
) {
    val drawable = GradientDrawable(orientation, colors).apply {
        this@apply.shape = shape
    }
    background = drawable
}


fun View.setGradientWithRadius(
    shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray,
    radius: Int = 0
) {
    val drawable = GradientDrawable(orientation, colors).apply {
        this@apply.shape = shape
        cornerRadius = dpToPx(radius).toFloat()
    }
    background = drawable
}


/**************************************************************************************************/


fun View.setSolidRect(@ColorInt color: Int = Color.parseColor("#FFFFFFFF")) {
    setSolid(GradientDrawable.RECTANGLE, color)
}

fun View.setSolidOval(@ColorInt color: Int = Color.parseColor("#FFFFFFFF")) {
    setSolid(GradientDrawable.OVAL, color)
}


fun View.setSolidRadiusRect(
    @ColorInt color: Int = Color.parseColor("#FFFFFFFF"), leftTop: Int = 0,
    leftBottom: Int = 0,
    rightTop: Int = 0,
    rightBottom: Int = 0
) {
    setSolidRadius(GradientDrawable.RECTANGLE, color, leftTop, leftBottom, rightTop, rightBottom)
}

fun View.setSolidRadiusRect(
    @ColorInt color: Int = Color.parseColor("#FFFFFFFF"), radius: Int
) {
    setSolidRadius(GradientDrawable.RECTANGLE, color, radius)
}

fun View.setSolidRadiusGradientRect(
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray,
    radius: Int
) {

    val drawable = GradientDrawable(orientation, colors).apply {
        this@apply.shape = GradientDrawable.RECTANGLE
        this@apply.cornerRadius = dpToPx(radius).toFloat()
    }
    background = drawable

}

fun View.setStrokeRect(
    width: Int, @ColorInt color: Int = Color.parseColor("#FFFFFFFF"),
    dashWidth: Int = 0,
    dashGap: Int = 0
) {
    setStroke(GradientDrawable.RECTANGLE, width, color, dashWidth, dashGap)
}

fun View.setStrokeOval(
    width: Int, @ColorInt color: Int = Color.parseColor("#FFFFFFFF"),
    dashWidth: Int = 0,
    dashGap: Int = 0
) {
    setStroke(GradientDrawable.OVAL, width, color, dashWidth, dashGap)
}


fun View.setGradientRect(
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray
) {
    setGradient(GradientDrawable.RECTANGLE, orientation, colors)

}

fun View.setGradientOval(
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray
) {
    setGradient(GradientDrawable.OVAL, orientation, colors)

}

fun View.setGradientLine(
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray
) {
    setGradient(GradientDrawable.LINE, orientation, colors)
}

fun View.setGradientRing(
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    colors: IntArray
) {
    setGradient(GradientDrawable.RING, orientation, colors)
}