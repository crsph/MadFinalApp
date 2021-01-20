package com.example.madfinalapp.model

import com.google.gson.annotations.SerializedName

data class TriviaList(
    @SerializedName("results")
    var triviaList: List<Trivia>
)