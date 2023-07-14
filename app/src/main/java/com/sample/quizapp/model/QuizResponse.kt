package com.sample.quizapp.model

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("response_code")
    var responseCode: Int? = null,
    @SerializedName("results")
    var results: ArrayList<Results> = arrayListOf()
)