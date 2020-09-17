package com.skarra.klotski.presentation.model

sealed class Route {
    object Fastest : Route()
    object Slowest : Route()
}