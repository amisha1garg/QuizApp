package com.sample.quizapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sample.quizapp.viewmodel.QuizViewModel

@Composable
fun QuizLayout(quizViewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        QuestionLayout(quizViewModel)
    }
}

@Composable
fun QuestionLayout(quizViewModel: QuizViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { quizViewModel.getQuizData() },
            modifier = Modifier.padding(vertical = 24.dp)
        )
        {
            Text(text = "Start Quiz")
        }
    }
    if (quizViewModel.showQuizQuestions)
        QuizScreen(quizViewModel)
}

@Composable
fun QuizScreen(quizViewModel: QuizViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (quizViewModel.quizEnded) {
            Text(
                text = "User ${quizViewModel.winnerIndex} wins!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Button(
                onClick = {
                    quizViewModel.showNextQuestion(
                        quizViewModel.selectedOptionUser1,
                        quizViewModel.selectedOptionUser2
                    )
                }
            ) {
                Text(text = "Next question")
            }
        }
        Text(
            text = "Score User1 : ${quizViewModel.user1Score}, Score User2 : ${quizViewModel.user2Score}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        QuizQuestionLayout(
            question = quizViewModel.currentQuestion,
            options = quizViewModel.currentQuestionOptions,
            quizViewModel = quizViewModel,
            selectedOption = quizViewModel.selectedOptionUser1,
            userNumber = "1",
        )
        QuizQuestionLayout(
            question = quizViewModel.currentQuestion,
            options = quizViewModel.currentQuestionOptions,
            quizViewModel = quizViewModel,
            selectedOption = quizViewModel.selectedOptionUser2,
            userNumber = "2",
        )
        ShowScores(quizViewModel = quizViewModel)
        ShowScoreCardButton(quizViewModel = quizViewModel)
        ShowTieBreakerButton(quizViewModel = quizViewModel)
    }
}

@Composable
fun QuizQuestionLayout(
    question: String,
    options: Array<String?>,
    quizViewModel: QuizViewModel,
    selectedOption: String?,
    userNumber: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "User $userNumber",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = question, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            if (option != null) {
                Option(option, selectedOption, quizViewModel, userNumber)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Option(
    option: String,
    selectedOption: String?,
    quizViewModel: QuizViewModel,
    userNumber: String
) {
    val isSelected = option == selectedOption

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(if (isSelected) Color.LightGray else Color.White)
            .clickable {
                if (userNumber == "1") {
                    quizViewModel.selectedOptionUser1 = option
                } else {
                    quizViewModel.selectedOptionUser2 = option
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                if (userNumber == "1") {
                    quizViewModel.selectedOptionUser1 = option
                } else {
                    quizViewModel.selectedOptionUser2 = option
                }
            },
            colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = option, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ShowScoreCardButton(quizViewModel: QuizViewModel) {
    if (quizViewModel.showGenerateScoreCardButton) {
        Button(
            onClick = {
                quizViewModel.showScores = true
            }
        ) {
            Text(text = "Generate Score card")
        }
    }
}

@Composable
fun ShowTieBreakerButton(quizViewModel: QuizViewModel) {
    if (quizViewModel.tiebreakerRound) {
        Button(
            onClick = {
                quizViewModel.startTieBreaker()
            }
        ) {
            Text(text = "Start Tie Breaker Round")
        }
    }
}

@Composable
fun ShowScores(quizViewModel: QuizViewModel) {
    if (quizViewModel.showScores)
        Text(
            text = "Score: Player 1: ${quizViewModel.user1Score}  Player 2: ${quizViewModel.user2Score}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
}

