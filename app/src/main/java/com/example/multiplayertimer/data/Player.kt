package com.example.multiplayertimer.data

data class Player (
    var active: Boolean = false,
    var onTurn: Boolean = false,
    var timeLeft: Long = 0L
)