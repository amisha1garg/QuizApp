package com.sample.quizapp.model

class QuizQuestion(
    val question: String,
    val answerOptions: Array<String?>,
    val correctAnswerIndex: Int
)