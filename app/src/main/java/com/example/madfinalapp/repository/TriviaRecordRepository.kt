package com.example.madfinalapp.repository

import android.content.Context
import com.example.madfinalapp.dao.TriviaDao
import com.example.madfinalapp.dao.TriviaRecordRoomDatabase
import com.example.madfinalapp.model.TriviaRecord

class TriviaRecordRepository(context: Context) {
    private var triviaDao: TriviaDao

    init {
        val triviaRecordRoomDatabase = TriviaRecordRoomDatabase.getDatabase(context)
        triviaDao = triviaRecordRoomDatabase!!.triviaRecordDao()
    }

    suspend fun getTriviaRecord(triviaCategory: String): List<TriviaRecord> {
        return triviaDao.getTriviaRecord(triviaCategory)
    }

    suspend fun getAllCategories(): List<String> {
        return triviaDao.getAllCategories()
    }

    suspend fun getTotalCorrectAnswers(): List<Int> {
        return triviaDao.getTotalCorrectAnswers()
    }

    suspend fun insertTriviaRecord(triviaRecord: List<TriviaRecord>) {
        triviaDao.insertTriviaRecord(triviaRecord)
    }

    suspend fun deleteTriviaRecord(category: String) {
        triviaDao.deleteTriviaRecord(category)
    }
}