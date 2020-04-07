package com.example.multiplayertimer.setting

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _navigateToTimer = MutableLiveData<Boolean>()
    val navigateToTimer: LiveData<Boolean>
        get() = _navigateToTimer

    private val _startTimeInMillis = MutableLiveData<Long>()
    val startTimeInMillis: LiveData<Long>
        get() = _startTimeInMillis

    val playerCount = MutableLiveData<Int>()

    init {
        playerCount.value = sharedPreferences.getInt("player_count", 4)
        _startTimeInMillis.value = sharedPreferences.getLong("start_time", 20 * 60 * 1000)
    }

    fun setStartTime(millis: Long) {
        _startTimeInMillis.value = millis
    }

    fun getMinutesFromStartTime() : Int = _startTimeInMillis.value!!.div(60000).toInt()

    fun getSecondsFromStartTime() : Int = _startTimeInMillis.value!!.rem(60000).div(1000).toInt()

    fun saveChanges() {
        sharedPreferences.edit {
            putInt("player_count", playerCount.value ?: 4)
            putLong("start_time", startTimeInMillis.value ?: 20 * 60 * 1000)
            commit()
        }
        _navigateToTimer.value = true
    }

    fun navigateToTimerComplete() {
        _navigateToTimer.value = false
    }
}