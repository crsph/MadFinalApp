package com.example.madfinalapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "triviaRecordTable")
data class TriviaRecord(

    @ColumnInfo(name = "triviaCategory")
    val triviaCategory: String,

    @ColumnInfo(name = "triviaQuestion")
    val triviaQuestion: String,

    @ColumnInfo(name = "correctAnswer")
    val correctTriviaAnswer: String,

    @ColumnInfo(name = "chosenAnswer")
    val chosenTriviaAnswer: String,

    @ColumnInfo(name = "isCorrect")
    val isTriviaAnswerCorrect: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)