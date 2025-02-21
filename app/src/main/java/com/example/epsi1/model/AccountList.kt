package com.example.epsi1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = User::class, parentColumns =  arrayOf("userId"), childColumns =  arrayOf("userId"))]
)
data class AccountList(
    @PrimaryKey(autoGenerate = true) val accountId: Int = 0,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "mdp") var mdp: String,
    @ColumnInfo(name = "site") var site: String,
    @ColumnInfo(name = "userId") val userId: Int,
)