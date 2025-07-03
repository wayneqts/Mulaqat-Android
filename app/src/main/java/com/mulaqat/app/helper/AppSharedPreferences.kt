package com.mulaqat.app.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mulaqat.app.model.Profile

class AppSharedPreferences(var context: Context) {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    fun setPf(pf: Profile?) {
        val pref = context.getSharedPreferences(AppConstrains.APP_PREF, Context.MODE_PRIVATE)
        val editor = pref.edit()
        val json = Gson().toJson(pf)
        editor.putString("profile", json).apply()
    }

    fun getPf(): Profile? {
        val pref = context.getSharedPreferences(AppConstrains.APP_PREF, Context.MODE_PRIVATE)
        val json = pref.getString("profile", null)
        val type = object : TypeToken<Profile>() {}.type
        return Gson().fromJson(json, type)
    }
}