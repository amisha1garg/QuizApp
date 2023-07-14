package com.sample.quizapp.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sample.quizapp.ui.theme.QuizAppTheme
import com.sample.quizapp.viewmodel.QuizViewModel

class QuizActivity : ComponentActivity() {

    private val quizViewModel = QuizViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizLayout(quizViewModel)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}


