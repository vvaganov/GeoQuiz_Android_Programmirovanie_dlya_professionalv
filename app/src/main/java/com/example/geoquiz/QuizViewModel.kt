package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    var currentIndex = 0
    var correctAnswerCount = 0
    var incorrectAnswerCount = 0
    val questionBankSize = questionBank.size
    var isCheater = false

    val getCurrentAnswer: Boolean get() = questionBank[currentIndex].answer
    val getQuestion: Int get() = questionBank[currentIndex].textResId
    fun moveToNext() = (currentIndex + 1) % questionBank.size
}