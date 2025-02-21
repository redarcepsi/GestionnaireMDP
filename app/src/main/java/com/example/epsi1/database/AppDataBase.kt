package com.example.epsi1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.epsi1.model.AccountDao
import com.example.epsi1.model.AccountList
import com.example.epsi1.model.User
import com.example.epsi1.model.UserDao

@Database(entities = arrayOf(User::class, AccountList::class), version = 1)

abstract class AppDatabase: RoomDatabase() {

    abstract fun UserDao(): UserDao
    abstract fun AccountDao(): AccountDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
//                    .createFromAsset("database/UserData.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}