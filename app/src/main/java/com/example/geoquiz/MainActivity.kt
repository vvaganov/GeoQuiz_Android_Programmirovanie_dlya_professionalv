package com.example.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionText: TextView
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        questionText = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        updateQuestion()

        questionText.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }


        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {

            checkAnswer(false)
        }

        prevButton.setOnClickListener {
            currentIndex = if (currentIndex >0)
                (currentIndex - 1) % questionBank.size
            else
                0
            updateQuestion()
        }


        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
    }

    private fun updateQuestion() {
        questionText.setText(questionBank[currentIndex].textResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}