package com.technical.authentication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.technical.authentication.model.User

@Database(
    entities = arrayOf(
        User::class
    ), version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}