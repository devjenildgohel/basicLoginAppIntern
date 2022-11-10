package com.example.basicloginapp.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class databaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "loginappdb";
    private static final String TABLE_NAME = "USERDATA";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String EMAIL_COL = "email";
    private static final String PASSWORD_COL = "password";
    private static final int DB_VERSION = 3;

    SQLiteDatabase db = this.getReadableDatabase();

    public databaseHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    public void addNewUser(String userName,String userEmail, String userPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL,userName);
        values.put(EMAIL_COL,userEmail);
        values.put(PASSWORD_COL,userPassword);

        db.insert(TABLE_NAME,null,values);
        db.close();

    }

    public String userCheck(String userName,String userEmail, String userPassword)
    {
        db = this.getReadableDatabase();
        String query = "SELECT email FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b="not found";
        if(cursor.moveToFirst()){
            do{
                a=cursor.getString(0);

                if(a.equals(userEmail)){
                    Log.d("TAG",b);
                    b=cursor.getString(0);
                    break;
                }
            }
            while (cursor.moveToNext());
            Log.d("TAG",b);
        }
        return b;
    }

    public boolean checkUserExist(String username, String password){
        String[] columns = {"email"};

        String selection = "email=? and password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
