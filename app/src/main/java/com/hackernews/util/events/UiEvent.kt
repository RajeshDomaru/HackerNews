package com.hackernews.util.events

import com.hackernews.util.UiText

sealed class UiEvent {

    data class SnackBarEvent(val uiText: UiText) : UiEvent()

    data class Toast(val uiText: UiText) : UiEvent()

}