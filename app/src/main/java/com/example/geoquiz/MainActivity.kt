package com.example.geoquiz

import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


private const val TAG = "MainActivity!"


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionText: TextView
    private lateinit var nextButton: Button

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var correctAnswerCount = 0
    private var incorrectAnswerCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bandle?) called ")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        questionText = findViewById(R.id.question_text_view)
        nextButton = findViewById(R.id.next_button)

        updateQuestion()

        questionText.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
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

        nextButton.setOnClickListener {
            if (correctAnswerCount + incorrectAnswerCount == questionBank.size) {
                questionText.text =
                    "Вы ответили на все вопросы. $correctAnswerCount правильных ответа и $incorrectAnswerCount не правильных"
                incorrectAnswerCount = 0
                correctAnswerCount = 0
            } else {
                currentIndex = (currentIndex + 1) % questionBank.size
                updateQuestion()
                falseButton.isEnabled = true
                trueButton.isEnabled = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart(Bandle?) called ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume(Bandle?) called ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause(Bandle?) called ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(Bandle?) called ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy(Bandle?) called ")
    }

    private fun updateQuestion() {
        questionText.setText(questionBank[currentIndex].textResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        var messageResId = 0
        if (userAnswer == correctAnswer) {
            correctAnswerCount++
            messageResId = R.string.correct_toast
        } else {
            incorrectAnswerCount++
            messageResId = R.string.incorrect_toast

        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}