package com.abdelrahman.amr.tahliluk_doctor.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager


import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("CustomSplashScreen")
class SplashScreenRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mSignInState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var mLanguage: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mDarkModeState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun getSignInState(context: Context): MutableStateFlow<Boolean> {
        mSignInState = MutableStateFlow(false)
        mPreferenceManager = PreferenceManager(context)
        val state = mPreferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)
        if (state) {
            this.mSignInState.emit(true)
        } else {
            this.mSignInState.emit(false)
        }
        return mSignInState
    }


}