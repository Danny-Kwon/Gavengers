package com.example.gavengers.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtil(context:Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pref_name", Context.MODE_PRIVATE)

    fun getInt(key: String, defValue: Int): Int{
        return prefs.getInt(key, defValue)
    }

    fun setInt(key: String, defValue: Int){
        prefs.edit().putInt(key, defValue).apply()
    }

    fun getString(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String){
        prefs.edit().putString(key, str).apply()
    }
}