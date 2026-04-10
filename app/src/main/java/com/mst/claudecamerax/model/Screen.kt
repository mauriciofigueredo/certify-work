package com.mst.claudecamerax.model

sealed class Screen {
    object Home : Screen()
    object Camera : Screen()
    object Preview : Screen()
}


