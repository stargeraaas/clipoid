package dev.sukharev.clipangel.utils.string

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import dev.sukharev.clipangel.R

class SpannableCropper(val context: Context) {

    fun span(inputText: String, text: String): Spannable? {

        val spanIndex = TextFinder.getFirstMatch(inputText, text)
        return spanIndex?.let {
            val croppedText = cropText(text, it)
            val spannable = SpannableString(croppedText)
            val croppedSpanIndex = TextFinder.getFirstMatch(inputText, croppedText)
            spannable.setSpan(ForegroundColorSpan(context.getColor(R.color.pantone_light_green)),
                    croppedSpanIndex!!.first,
                    croppedSpanIndex.second,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

            spannable.setSpan(BackgroundColorSpan(context.getColor(R.color.pantone_orange)),
                    croppedSpanIndex.first,
                    croppedSpanIndex.second,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

            spannable.setSpan(StyleSpan(Typeface.BOLD),
                    croppedSpanIndex.first,
                    croppedSpanIndex.second,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spannable
        } ?: run {
            null
        }
    }

    private fun cropText(text: String, matchIndex: Pair<Int, Int>, startOffset: Int = 5,
                         endOffset: Int = text.length - 1): String {
        val startIndex = if (matchIndex.first - startOffset > 0) matchIndex.first - startOffset else 0
        val endIndex = endOffset
        return "${if (startIndex == 0) "" else "..."}${text.slice(IntRange(startIndex, endIndex)) }"
    }

}