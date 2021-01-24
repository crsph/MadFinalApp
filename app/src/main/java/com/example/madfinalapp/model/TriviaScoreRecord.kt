package com.example.madfinalapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "triviaScoreRecordTable")
data class TriviaScoreRecord(

    @ColumnInfo(name = "triviaCategory")
    val triviaCategory: String,

    @ColumnInfo(name = "totalCorrectAnswers")
    val totalCorrectAnswers: Double,

    @ColumnInfo(name = "totalWrongAnswers")
    val totalWrongAnswers: Double,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)