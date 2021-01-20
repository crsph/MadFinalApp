package com.example.madfinalapp.dao

import androidx.room.*
import com.example.madfinalapp.model.TriviaRecord

@Dao
interface TriviaDao {

    @Query("SELECT * FROM triviaRecordTable")
    suspend fun getAllTriviaRecord(): List<TriviaRecord>

    @Query("SELECT triviaCategory FROM triviaRecordTable GROUP BY triviaCategory")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT SUM(isCorrect) FROM triviaRecordTable WHERE isCorrect = 1 GROUP BY triviaCategory")
    suspend fun getTotalCorrectAnswers(): List<Int>

    @Insert
    suspend fun insertTriviaRecord(triviaRecord: TriviaRecord)

    @Delete
    suspend fun deleteTriviaRecord(triviaRecord: TriviaRecord)

    @Update
    suspend fun updateTriviaRecord(triviaRecord: TriviaRecord)

    @Query("DELETE FROM triviaRecordTable")
    suspend fun nukeTable()
}