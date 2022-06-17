package com.amrmedhatandroid.tahliluk_laboratory.database

import android.content.Context
import android.content.SharedPreferences
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants


class PreferenceManager(context: Context) {

    private var mSharedPreferences: SharedPreferences? = null

    init {
        mSharedPreferences =
            context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor? = mSharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(key: String): Boolean {
        return mSharedPreferences!!.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        val editor: SharedPreferences.Editor? = mSharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String): String? {
        return mSharedPreferences!!.getString(key, null)
    }

    fun clear() {
        val editor: SharedPreferences.Editor? = mSharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}