package com.example.savingstuff

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Myname::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun nameDao() : NameDao
}