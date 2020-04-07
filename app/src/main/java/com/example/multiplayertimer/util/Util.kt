package com.example.multiplayertimer.util

import java.text.SimpleDateFormat
import java.util.*

fun millisToTime(millis: Long) : String = SimpleDateFormat("mm:ss").format(Date(millis))