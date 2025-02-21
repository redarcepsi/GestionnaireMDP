package com.example.epsi1.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getalluser(): List<User>
}