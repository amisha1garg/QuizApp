package com.sample.quizapp.model

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class Results(
    @SerializedName("question")
    var question: String? = null,
    @SerializedName("correct_answer")
    var correctAnswer: String? = null,
    @SerializedName("incorrect_answers")
    var incorrectAnswers: JSONArray? = null
)