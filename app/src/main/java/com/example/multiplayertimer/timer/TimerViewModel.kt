package com.example.multiplayertimer.timer

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.multiplayertimer.R
import com.example.multiplayertimer.data.Player
import java.util.*

class TimerViewModel(private val sharedPreferences: SharedPreferences, application: Application) : AndroidViewModel(application) {
    private val _playerOne = MutableLiveData<Player>()
    val playerOne: LiveData<Player>
        get() = _playerOne

    private val _playerTwo = MutableLiveData<Player>()
    val playerTwo: LiveData<Player>
        get() = _playerTwo

    private val _playerThree = MutableLiveData<Player>()
    val playerThree: LiveData<Player>
        get() = _playerThree

    private val _playerFour = MutableLiveData<Player>()
    val playerFour: LiveData<Player>
        get() = _playerFour

    private var _playerToKickPos = MutableLiveData<Int>()
    val playerToKickPos: LiveData<Int>
        get() = _playerToKickPos

    private lateinit var gameTimer: CountDownTimer
    private var mediaPlayer: MediaPlayer = MediaPlayer.create(application, R.raw.sfx)
    private var runningPlayer: MutableLiveData<Player>
    private val players = LinkedList<MutableLiveData<Player>>()
    var gameStarted = false
    var isPaused = true

    init {
        setupPlayers()
        runningPlayer = players.poll()!!
    }

    private fun startTimer() {
        if(players.size > 0) {
            val currentPlayer = runningPlayer.value!!
            currentPlayer.onTurn = true
            gameTimer = object : CountDownTimer(currentPlayer.timeLeft, 250) {
                override fun onFinish() {
                    currentPlayer.active = false
                    runningPlayer.value = currentPlayer
                    nextPlayer(false)
                }

                override fun onTick(millisUntilFinished: Long) {
                    currentPlayer.timeLeft = millisUntilFinished
                    runningPlayer.value = currentPlayer
                }
            }.start()
        }
    }

    fun kickPlayer(position: Int) {
        if(gameStarted) {
            val playerCount = players.size + 1
            if (playerCount > 1) _playerToKickPos.value = position
        }
    }

    fun cancelKick() {
        _playerToKickPos.value = null
    }

    fun continueKick() {
        val kickedPlayerContainer = when (_playerToKickPos.value) {
            1 -> _playerOne
            2 -> _playerTwo
            3 -> _playerThree
            else -> _playerFour
        }

        val kickedPlayer = kickedPlayerContainer.value!!
        kickedPlayer.active = false
        kickedPlayerContainer.value = kickedPlayer

        if (kickedPlayerContainer == runningPlayer) nextPlayer(true)
        else players.remove(kickedPlayerContainer)
    }

    fun nextPlayer(ignoreGameState: Boolean) {
        val prevGameState = isPaused
        val currentPlayer = runningPlayer.value!!

        if(prevGameState && !ignoreGameState) return
        if(!prevGameState) toggleGameState(false)

        if(currentPlayer.active) {
            currentPlayer.onTurn = false
            runningPlayer.value = currentPlayer
            players.add(runningPlayer)
        }

        runningPlayer = players.poll()!!
        val nextPlayer = runningPlayer.value!!
        nextPlayer.onTurn = true
        runningPlayer.value = nextPlayer

        if(!prevGameState) toggleGameState(false)
    }

    private fun stopTimer() {
        gameTimer.cancel()
    }

    private fun setupPlayers() {
        val playerCount = sharedPreferences.getInt("player_count", 4)
        val startTimeInMillis = sharedPreferences.getLong("start_time", 20 * 60 * 1000)

        _playerOne.value = Player(true, false, startTimeInMillis)
        _playerTwo.value = Player(true, false, startTimeInMillis)
        _playerThree.value = if(playerCount >= 3) Player(true, false, startTimeInMillis) else Player()
        _playerFour.value = if(playerCount >= 4) Player(true, false, startTimeInMillis) else Player()

        players.clear()
        players.addAll(listOf(_playerOne, _playerThree,  _playerTwo, _playerFour).filter {it.value!!.active})
    }

    fun toggleGameState(fromCenterButton: Boolean) {
        if(!gameStarted) gameStarted = true
        isPaused = !isPaused
        if(!isPaused) {
            startTimer()
        } else {
            if(fromCenterButton) mediaPlayer.start()
            stopTimer()
        }
    }

    fun resetGameState() {
        stopTimer()
        setupPlayers()
        isPaused = true
        gameStarted = false
        runningPlayer = players.poll()
    }
}