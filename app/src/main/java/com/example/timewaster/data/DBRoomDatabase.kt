package com.example.timewaster.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DB::class], version = 2, exportSchema = false)
abstract class DBRoomDatabase : RoomDatabase() {

    abstract fun DBDao(): DBDao

    companion object {
        @Volatile
        private var INSTANCE: DBRoomDatabase? = null
        fun getDatabase(context: Context): DBRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBRoomDatabase::class.java,
                    "DB_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
