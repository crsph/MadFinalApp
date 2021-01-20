package com.example.madfinalapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.madfinalapp.model.TriviaRecord

@Database(entities = [TriviaRecord::class], version = 1, exportSchema = false)
abstract class TriviaRecordRoomDatabase : RoomDatabase() {

    abstract fun triviaRecordDao(): TriviaDao

    companion object {
        private const val DATABASE_NAME = "TRIVIA_RECORD_DATABASE"

        @Volatile
        private var triviaRecordRoomDatabaseInstance: TriviaRecordRoomDatabase? = null

        fun getDatabase(context: Context): TriviaRecordRoomDatabase? {
            if (triviaRecordRoomDatabaseInstance == null) {
                synchronized(TriviaRecordRoomDatabase::class.java) {
                    if (triviaRecordRoomDatabaseInstance == null) {
                        triviaRecordRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            TriviaRecordRoomDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return triviaRecordRoomDatabaseInstance
        }
    }

}