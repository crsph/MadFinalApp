package com.example.madfinalapp.dao

import androidx.room.*
import com.example.madfinalapp.model.TriviaRecord
import com.example.madfinalapp.model.TriviaScoreRecord

@Dao
interface TriviaDao {

    @Query("SELECT * FROM triviaRecordTable")
    suspend fun getAllTriviaRecord(): List<TriviaRecord>

    @Query("SELECT triviaCategory FROM triviaRecordTable GROUP BY triviaCategory")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT totalCorrectAnswers FROM triviaScoreRecordTable")
    suspend fun getTotalCorrectAnswers(): List<Double>

    @Insert
    suspend fun insertTriviaRecord(triviaRecord: List<TriviaRecord>)

    @Insert
    suspend fun insertTriviaScoreRecord(triviaScoreRecord: TriviaScoreRecord)

    @Query("DELETE FROM triviaRecordTable WHERE triviaCategory = :category")
    suspend fun deleteTriviaRecord(category: String)

    @Update
    suspend fun updateTriviaRecord(triviaRecord: List<TriviaRecord>)

    @Query("DELETE FROM triviaRecordTable")
    suspend fun nukeTable()
}