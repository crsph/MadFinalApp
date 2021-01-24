package com.example.madfinalapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.madfinalapp.model.TriviaRecord
import com.example.madfinalapp.model.TriviaScoreRecord

@Database(entities = [TriviaRecord::class, TriviaScoreRecord::class], version = 2, exportSchema = false)
abstract class TriviaRecordRoomDatabase : RoomDatabase() {

    abstract fun triviaRecordDao(): TriviaDao

    companion object {
        private const val DATABASE_NAME = "TRIVIA_RECORD_DATABASE"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE triviaScoreRecordTable ADD COLUMN ")
            }
        }

        @Volatile
        private var triviaRecordRoomDatabaseInstance: TriviaRecordRoomDatabase? = null

        fun getDatabase(context: Context): TriviaRecordRoomDatabase? {
            if (triviaRecordRoomDatabaseInstance == null) {
                synchronized(TriviaRecordRoomDatabase::class.java) {
                    if (triviaRecordRoomDatabaseInstance == null) {
                        triviaRecordRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            TriviaRecordRoomDatabase::class.java, DATABASE_NAME
                        ).addMigrations(MIGRATION_1_2).build()
                    }
                }
            }
            return triviaRecordRoomDatabaseInstance
        }
    }
}