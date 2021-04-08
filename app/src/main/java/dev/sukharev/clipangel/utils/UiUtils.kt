package dev.sukharev.clipangel.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

fun TextView.setCursorDrawableColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                intArrayOf(color, color))
        gradientDrawable.setSize(2.spToPx(context).toInt(), textSize.toInt())
        textCursorDrawable = gradientDrawable
        return
    }

    try {
        val editorField = try {
            TextView::class.java.getDeclaredField("mEditor").apply { isAccessible = true }
        } catch (t: Throwable) {
            null
        }
        val editor = editorField?.get(this) ?: this
        val editorClass: Class<*> = if (editorField == null) TextView::class.java else editor.javaClass

        val tintedCursorDrawable = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                .apply { isAccessible = true }
                .getInt(this)
                .let { ContextCompat.getDrawable(context, it) ?: return }
                .let { tintDrawable(it, color) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            editorClass
                    .getDeclaredField("mDrawableForCursor")
                    .apply { isAccessible = true }
                    .run { set(editor, tintedCursorDrawable) }
        } else {
            editorClass
                    .getDeclaredField("mCursorDrawable")
                    .apply { isAccessible = true }
                    .run { set(editor, arrayOf(tintedCursorDrawable, tintedCursorDrawable)) }
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}

fun Number.spToPx(context: Context? = null): Float {
    val res = context?.resources ?: android.content.res.Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), res.displayMetrics)
}

fun tintDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
    (drawable as? VectorDrawableCompat)
            ?.apply { setTintList(ColorStateList.valueOf(color)) }
            ?.let { return it }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        (drawable as? VectorDrawable)
                ?.apply { setTintList(ColorStateList.valueOf(color)) }
                ?.let { return it }
    }

    val wrappedDrawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrappedDrawable, color)
    return DrawableCompat.unwrap(wrappedDrawable)
}