package com.example.epsi1.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AccountDao {
    @Query("SELECT * FROM AccountList WHERE userId=:userid ORDER BY accountId DESC")
    fun getallaccount(userid:Int=1): List<AccountList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addaccount(accounts:AccountList)

    @Query("UPDATE AccountList SET email = :newemail ,mdp = :newmdp , site = :newsite WHERE accountId= :accountid")
    fun updateaccount(newemail:String,newmdp:String,newsite:String,accountid:Int)

    @Query("DELETE FROM AccountList WHERE accountId= :accountid")
    fun deleteaccount(accountid: Int)
}