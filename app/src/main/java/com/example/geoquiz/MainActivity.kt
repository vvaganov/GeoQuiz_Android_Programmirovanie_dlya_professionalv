package com.example.geoquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

const val TAG = "MainActivity!"
private const val INDEX_KEY = "currentIndex"
private const val CORRECT_ANSWER_KEY = "correctAnswerKey"
private const val INCORRECT_ANSWER_KEY = "incorrectAnswerKey"



class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionText: TextView
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button

    private val quizViewModel by lazy { ViewModelProvider(this)[QuizViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called ")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(INDEX_KEY) ?: 0
        quizViewModel.currentIndex = currentIndex
        val correctAnswerCount = savedInstanceState?.getInt(CORRECT_ANSWER_KEY) ?: 0
        quizViewModel.correctAnswerCount = correctAnswerCount
        val incorrectAnswerCount = savedInstanceState?.getInt(INCORRECT_ANSWER_KEY) ?: 0
        quizViewModel.incorrectAnswerCount = incorrectAnswerCount
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        questionText = findViewById(R.id.question_text_view)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)

        updateQuestion()

        questionText.setOnClickListener {
            quizViewModel.currentIndex = quizViewModel.getQuestion
            updateQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.getCurrentAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            startActivity(intent)
        }

        nextButton.setOnClickListener {
            if (quizViewModel.correctAnswerCount + quizViewModel.incorrectAnswerCount == quizViewModel.questionBankSize) {

                questionText.text =
                    "Вы ответили на все вопросы. ${quizViewModel.correctAnswerCount} правильных ответа и ${quizViewModel.incorrectAnswerCount} не правильных"
                quizViewModel.incorrectAnswerCount = 0
                quizViewModel.correctAnswerCount = 0
            } else {
                quizViewModel.currentIndex = quizViewModel.moveToNext()
                updateQuestion()
                falseButton.isEnabled = true
                trueButton.isEnabled = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called ")
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause() called ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(INDEX_KEY, quizViewModel.currentIndex)
        outState.putInt(CORRECT_ANSWER_KEY, quizViewModel.correctAnswerCount)
        outState.putInt(INCORRECT_ANSWER_KEY, quizViewModel.incorrectAnswerCount)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called ")
    }

    private fun updateQuestion() {
        questionText.setText(quizViewModel.getQuestion)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.getCurrentAnswer

        var messageResId = 0
        if (userAnswer == correctAnswer) {
            quizViewModel.correctAnswerCount++
            messageResId = R.string.correct_toast
        } else {
            quizViewModel.incorrectAnswerCount++
            messageResId = R.string.incorrect_toast

        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}