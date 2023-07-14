package com.sample.quizapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sample.quizapp.model.QuizQuestion
import org.json.JSONException

class QuizViewModel(private val context: Context) : ViewModel(), DefaultLifecycleObserver {

    private var quizQuestions: ArrayList<QuizQuestion> by mutableStateOf(arrayListOf())
    private var currentQuestionCorrectAnswerIndex by mutableStateOf(0)
    private var currentQuestionIndex = 0
    var showQuizQuestions by mutableStateOf(false)
    var showGenerateScoreCardButton by mutableStateOf(false)
    var showScores by mutableStateOf(false)
    var currentQuestion by mutableStateOf("")
    var currentQuestionOptions: Array<String?> by mutableStateOf(emptyArray())
    var user1Score by mutableStateOf(0)
    var user2Score by mutableStateOf(0)
    var winnerIndex by mutableStateOf(-1)
    var tiebreakerRound by mutableStateOf(false)
    var quizEnded by mutableStateOf(false)
    var selectedOptionUser1 by mutableStateOf<String?>(null)
    var selectedOptionUser2 by mutableStateOf<String?>(null)

    fun getQuizData() {
        clearData()
        val queue = Volley.newRequestQueue(context)
        val url = "https://opentdb.com/api.php?amount=5"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val results = response.getJSONArray("results")
                    println("Ami $results")
                    for (i in 0 until results.length()) {
                        val result = results.getJSONObject(i)
                        val question = result.getString("question")
                        val correctAnswer = result.getString("correct_answer")
                        val incorrectAnswers = result.getJSONArray("incorrect_answers")
                        val answerOptions = arrayOfNulls<String>(incorrectAnswers.length() + 1)
                        for (j in 0 until incorrectAnswers.length()) {
                            answerOptions[j] = incorrectAnswers.getString(j)
                        }
                        answerOptions[incorrectAnswers.length()] = correctAnswer
                        val correctAnswerIndex = incorrectAnswers.length()

                        val quizQuestion = QuizQuestion(question, answerOptions, correctAnswerIndex)
                        quizQuestions.add(quizQuestion)
                    }
                    showNextQuestion(
                        selectedOptionUser1,
                        selectedOptionUser2
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> error.printStackTrace() })
        queue.add(jsonObjectRequest)
        showQuizQuestions = true
    }

    fun showNextQuestion(selectedAnsUser1: String?, selectedAnsUser2: String?) {
        if (selectedAnsUser1 != null) {
            if (isCorrectAnswer(selectedAnsUser1)) {
                user1Score += 5
            } else {
                user1Score -= 2
            }
        }
        if (selectedAnsUser2 != null) {
            if (isCorrectAnswer(selectedAnsUser2)) {
                user2Score += 5
            } else {
                user2Score -= 2
            }
        }
        currentQuestion = quizQuestions[currentQuestionIndex].question
        currentQuestionOptions = quizQuestions[currentQuestionIndex].answerOptions
        currentQuestionCorrectAnswerIndex = quizQuestions[currentQuestionIndex].correctAnswerIndex

        if (currentQuestionIndex == quizQuestions.size - 1 && user1Score != user2Score) {
            winnerIndex = if (user1Score > user2Score) {
                1
            } else {
                2
            }
            // TODO : handle quiz ended and generate score card vars
            quizEnded = true
            showGenerateScoreCardButton = true
            tiebreakerRound = false
        } else if (currentQuestionIndex == quizQuestions.size - 1 && user1Score == user2Score)
            tiebreakerRound = true

        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex++
        }

        selectedOptionUser1 = null
        selectedOptionUser2 = null
    }

    private fun clearData() {
        quizQuestions.clear()
        currentQuestionIndex = 0
        currentQuestion = ""
        currentQuestionOptions = emptyArray()
        currentQuestionCorrectAnswerIndex = 0
        tiebreakerRound = false
        showGenerateScoreCardButton = false
    }

    private fun isCorrectAnswer(selectedOption: String): Boolean {
        return selectedOption == currentQuestionOptions[currentQuestionCorrectAnswerIndex]
    }

    /*fun generateScoreCard(context: Context) {
        val fileName = "Scorecard.pdf"
        val file = File("/Documents/scoreCard.pdf")

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
        }

        canvas.drawText("Scorecard", 220f, 100f, paint)

        paint.textSize = 30f

        canvas.drawText("Player name: User 1", 50f, 200f, paint)
        canvas.drawText("Score: $user1Score", 50f, 250f, paint)

        canvas.drawText("Player name: User 2", 50f, 200f, paint)
        canvas.drawText("Score: $user2Score", 50f, 250f, paint)

        pdfDocument.finishPage(page)

        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val fileUri = FileProvider.getUriForFile(
            context,
            "com.sample.quizapp.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        context.startActivity(intent)
    }
*/
    fun startTieBreaker() {
        getQuizData()
        quizEnded = false
    }
}