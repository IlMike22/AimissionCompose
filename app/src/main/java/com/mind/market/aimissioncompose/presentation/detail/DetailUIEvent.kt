package com.mind.market.aimissioncompose.presentation.detail

sealed class DetailUIEvent<T>(val data: T? = null) {
    class ShowValidationResult<T>(statusCode: T? = null) : DetailUIEvent<T>(data = statusCode)
    class HideKeyboard<T> : DetailUIEvent<T>()
    class NavigateToLandingPage<T> : DetailUIEvent<T>()
    class NavigateToSettings<T>: DetailUIEvent<T>()
    class NavigateToInfo<T>: DetailUIEvent<T>()
}