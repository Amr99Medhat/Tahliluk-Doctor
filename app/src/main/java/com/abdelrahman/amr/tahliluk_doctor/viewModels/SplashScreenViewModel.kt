package com.abdelrahman.amr.tahliluk_doctor.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.abdelrahman.amr.tahliluk_doctor.repositories.SplashScreenRepository
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModel : ViewModel() {
    private var mSplashScreenRepository: SplashScreenRepository = SplashScreenRepository()

    suspend fun getSignInState(context: Context): MutableStateFlow<Boolean> {
        return mSplashScreenRepository.getSignInState(context)
    }

}