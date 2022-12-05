package com.example.basicloginapp.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userdata")
class UserDataClass(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "user_email") var userEmail: String,
    @ColumnInfo(name = "user_password") var userPassword: String,
)