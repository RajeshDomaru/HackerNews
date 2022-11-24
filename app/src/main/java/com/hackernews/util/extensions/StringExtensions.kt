package com.hackernews.util.extensions

fun String?.validateString() = if (isNullOrEmpty()) "" else trim()
