package com.example.geoquiz

import android.app.Activity
import android.app.ComponentCaller
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
private const val REQUEST_CODE_CHEAT = 0

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
            quizViewModel.currentIndex = quizViewModel.moveToNext()
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
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
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

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}