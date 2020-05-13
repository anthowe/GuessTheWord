package com.example.android.guesstheword.screens.game

import android.content.IntentSender
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish


    //current time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    //The string version of current time
    val currentTimeString = Transformations.map(currentTime){
        time -> DateUtils.formatElapsedTime(time)
    }

    //Current word hint
    val wordHint = Transformations.map(word){ word ->
        val randomPosition = (1..word.length).random()
        "Current word has " + word.length + " letters" + "\nThe letter at position " + randomPosition +
                " is " + word.get(randomPosition-1).toUpperCase()
    }
    private val timer: CountDownTimer
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        } else {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        }

    }

    init {
        _word.value = ""
        _score.value = 0

        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
    override fun onTick(millisUntilFinished: Long) {
        _currentTime.value = millisUntilFinished/ ONE_SECOND
    }

    override fun onFinish() {
        _currentTime.value = DONE
        onGameFinish()
    }
}
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel the timer
        timer.cancel()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        //score--
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        // score++
        nextWord()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    companion object {
        //Time when game is over
        private const val DONE = 0L

        //Countdown timer interval
        private const val ONE_SECOND = 1000L

        //Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

}