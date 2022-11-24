package com.hackernews.util

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    data class DynamicString(val value: String) : UiText()

    class StringResource(@StringRes val resIds: Int, vararg val args: Any) : UiText()

    fun asString(context: Context): String {

        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resIds, args)
        }

    }

    fun isNotEmpty(context: Context): Boolean = asString(context).isNotEmpty()

}
