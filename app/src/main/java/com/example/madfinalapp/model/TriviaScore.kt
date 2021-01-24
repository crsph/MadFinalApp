package com.example.madfinalapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "triviaScoreTable")
data class TriviaScore(

    @ColumnInfo(name = "triviaCategory")
    val triviaCategory: String,

    @Ignore
    @ColumnInfo(name = "totalCorrectAnswers")
    val triviaTotalCorrect: Int

)