package com.example.madfinalapp.model

import com.google.gson.annotations.SerializedName

data class TriviaIncorrectAnswerList(
    @SerializedName("incorrect_answers")
    var incorrectAnswer: List<String>
)
