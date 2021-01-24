package com.example.madfinalapp.repository

import android.content.Context
import com.example.madfinalapp.dao.TriviaDao
import com.example.madfinalapp.dao.TriviaRecordRoomDatabase
import com.example.madfinalapp.model.TriviaRecord
import com.example.madfinalapp.model.TriviaScore

class TriviaRecordRepository(context: Context) {
    private var triviaDao: TriviaDao

    init {
        val triviaRecordRoomDatabase = TriviaRecordRoomDatabase.getDatabase(context)
        triviaDao = triviaRecordRoomDatabase!!.triviaRecordDao()
    }

    suspend fun getAllTriviaRecords(): List<TriviaRecord> {
        return triviaDao.getAllTriviaRecord()
    }

    suspend fun getAllCategories(): List<String> {
        return triviaDao.getAllCategories()
    }

    suspend fun getTotalCorrectAnswers(): List<TriviaScore> {
        return triviaDao.getTotalCorrectAnswers()
    }

    suspend fun insertTriviaRecord(triviaRecord: List<TriviaRecord>) {
        triviaDao.insertTriviaRecord(triviaRecord)
    }

    suspend fun deleteTriviaRecord(category: String) {
        triviaDao.deleteTriviaRecord(category)
    }

    suspend fun updateTriviaRecord(triviaRecord: List<TriviaRecord>) {
        triviaDao.updateTriviaRecord(triviaRecord)
    }

    suspend fun deleteAll() {
        triviaDao.nukeTable()
    }
}