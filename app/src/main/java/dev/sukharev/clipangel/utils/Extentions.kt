package dev.sukharev.clipangel.utils

import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import dev.sukharev.clipangel.core.App
import java.text.SimpleDateFormat
import java.util.*

fun String.copyInClipboard() {
    val clipboard = App.app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", this)
    clipboard.setPrimaryClip(clip)
}

fun String.copyInClipboardWithToast(text: String) {
    copyInClipboard()
    Toast.makeText(App.app, text, Toast.LENGTH_SHORT).show()
}

fun String.copyInClipboardWithToast(resId: Int) {
    copyInClipboard()
    Toast.makeText(App.app, App.app.getString(resId), Toast.LENGTH_SHORT).show()
}

fun Long.toDateFormat1(): String {
    val pattern = "d MMM yyyy HH:mm:ss"
    val locale = Locale.getDefault()
    val simpleDateFormat = SimpleDateFormat(pattern, locale)
    return simpleDateFormat.format(Date(this))
}

fun View.addCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}