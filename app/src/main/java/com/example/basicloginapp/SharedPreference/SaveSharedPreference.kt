package com.example.basicloginapp.SharedPreference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.basicloginapp.SharedPreference.SaveSharedPreference

object SaveSharedPreference {
    const val PREF_USER_EMAIL = "EMAIL"
    fun getSharedPreference(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setPrefUserEmail(context: Context?, userEmail: String?) {
        val editor = getSharedPreference(context).edit()
        editor.putString(PREF_USER_EMAIL, userEmail)
        editor.commit()
    }

    fun getPrefUserEmail(context: Context?): String? {
        return getSharedPreference(context).getString(PREF_USER_EMAIL, "")
    }

    fun clearUserEmail(context: Context?) {
        val editor = getSharedPreference(context).edit()
        editor.clear()
        editor.commit()
    }
}