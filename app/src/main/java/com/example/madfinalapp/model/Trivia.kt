package com.example.madfinalapp.model

import com.google.gson.annotations.SerializedName

data class Trivia(
    @SerializedName("category")
    val category: String,

    @SerializedName("question")
    val question: String,

    @SerializedName("correct_answer")
    val correct_answer: String,

    @SerializedName("incorrect_answers")
    val incorrect_answers: List<String>
)