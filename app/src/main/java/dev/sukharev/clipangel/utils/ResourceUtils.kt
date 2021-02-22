package dev.sukharev.clipangel.utils

import android.content.res.TypedArray
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getIntOrThrow
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.res.getStringOrThrow
import java.lang.Exception

fun getStringOrNullAttr(attrs: TypedArray, attrId: Int): String? = try {
    attrs.getStringOrThrow(attrId)
} catch (e: Exception) {
    null
}

fun getIntOrNullAttr(attrs: TypedArray, attrId: Int): Int? = try {
    attrs.getIntOrThrow(attrId)
} catch (e: Exception) {
    null
}

fun getResourceOrNullAttr(attrs: TypedArray, attrId: Int): Int? = try {
    attrs.getResourceIdOrThrow(attrId)
} catch (e: Exception) {
    null
}

fun getColorOrNullAttr(attrs: TypedArray, attrId: Int): Int? = try {
    attrs.getColorOrThrow(attrId)
} catch (e: Exception) {
    null
}