package com.example.basicloginapp.databaseHandler

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.util.Log

class databaseHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private var db = this.readableDatabase
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)")

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query)
    }

    fun addNewUser(userName: String?, userEmail: String?, userPassword: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_COL, userName)
        values.put(EMAIL_COL, userEmail)
        values.put(PASSWORD_COL, userPassword)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun userCheck(userEmail: String): String {
        db = this.readableDatabase
        val query = "SELECT email FROM " + TABLE_NAME
        val cursor = db.rawQuery(query, null)
        var a: String
        var b: String
        b = "not found"
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0)
                if (a == userEmail) {
                    Log.d("TAG", b)
                    b = cursor.getString(0)
                    break
                }
            } while (cursor.moveToNext())
            Log.d("TAG", b)
        }
        cursor.close()
        return b
    }

    fun isUserExist(userMail: String, userPassword: String): Boolean {
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery(
            "Select * from  USERDATA where email = ? and password = ?",
            arrayOf(userMail, userPassword),
            null
        )
        return cursor.count > 0
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "loginappdb.db"
        private const val TABLE_NAME = "USERDATA"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val EMAIL_COL = "email"
        private const val PASSWORD_COL = "password"
        private const val DB_VERSION = 3
    }
}