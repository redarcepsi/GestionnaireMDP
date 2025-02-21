package com.example.epsi1

import android.app.Application
import com.example.epsi1.database.AppDatabase

class UserDataApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}