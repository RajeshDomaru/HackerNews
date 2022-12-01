package com.hackernews.util.extensions

fun String?.validateString() = if (isNullOrEmpty()) "" else trim()

fun String?.isValidString(): Boolean {

    if (this.isNullOrEmpty()) return false

    return this.trim { it <= ' ' }.isNotEmpty() &&

            !this.trim { it <= ' ' }.equals("null", ignoreCase = true) &&

            !this.trim { it <= ' ' }.equals("", ignoreCase = true)

}
