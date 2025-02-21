package com.example.epsi1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "mdp") val mdp: String,
)