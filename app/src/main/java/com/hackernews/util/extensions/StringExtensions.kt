package com.hackernews.util.extensions

import android.content.Context

fun String?.validateString() = if (isNullOrEmpty()) "" else trim()

fun Context.getStringResources(stringId: Int?): String {

    return if (stringId != null) {

        try {

            resources.getString(stringId)

        } catch (e: Exception) {

            e.printStackTrace()

            ""

        }

    } else ""

}

fun String?.isValidString(): Boolean {

    if (this.isNullOrEmpty()) return false

    return this.trim { it <= ' ' }.isNotEmpty() &&

            !this.trim { it <= ' ' }.equals("null", ignoreCase = true) &&

            !this.trim { it <= ' ' }.equals("", ignoreCase = true)

}
