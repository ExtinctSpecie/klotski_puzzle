package com.skarra.klotski.presentation.model

sealed class MainActivityViewState {
    object Loading : MainActivityViewState()
    object NoLoading : MainActivityViewState()
}