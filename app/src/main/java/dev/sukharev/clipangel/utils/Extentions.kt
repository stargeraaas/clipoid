package dev.sukharev.clipangel.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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

fun Long.toDateFormat1(): String {
    val pattern = "d MMM yyyy HH:mm:ss"
    val locale = Locale.getDefault()
    val simpleDateFormat = SimpleDateFormat(pattern, locale)
    return simpleDateFormat.format(Date(this))
}