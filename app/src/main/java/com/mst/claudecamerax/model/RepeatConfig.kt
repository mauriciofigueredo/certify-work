package com.mst.claudecamerax.model

data class RepeatConfig (
    val totalShots: Int = 4,
    val currentIndex: Int = 1
){
    val isDone: Boolean get() = currentIndex > totalShots
    val next: RepeatConfig get() = copy(currentIndex = currentIndex + 1)
}


