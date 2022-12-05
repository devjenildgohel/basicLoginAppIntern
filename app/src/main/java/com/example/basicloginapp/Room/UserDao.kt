package com.example.basicloginapp.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun insert(userDataClass: UserDataClass)

    @Update
    fun update(userDataClass: UserDataClass)

    @Delete
    fun delete(userDataClass: UserDataClass)

    @Query("select * FROM USERDATA")
    fun getAllUsers() : LiveData<List<UserDataClass>>

    @Query("select * from userdata where user_email == :userEmail")
    fun isUserExists(userEmail: String) : Boolean

    @Query("delete from USERDATA")
    fun deleteAllUsers()

    @Query("select * from userdata where user_email == :userEmail and user_password == :userPassword")
    fun isUserMatches(userEmail: String, userPassword:String) : Boolean

}