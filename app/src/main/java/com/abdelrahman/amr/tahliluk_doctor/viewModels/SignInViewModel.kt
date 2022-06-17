package com.abdelrahman.amr.tahliluk_doctor.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.amr.tahliluk_doctor.models.Doctor
import com.abdelrahman.amr.tahliluk_doctor.repositories.SignInRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val mSignInRepository: SignInRepository = SignInRepository()

    suspend fun signIn(
        inputNumber: String,
        inputPassword: String
    ): MutableStateFlow<Doctor> {
        onCleared()
        return mSignInRepository.signIn(
            inputNumber,
            inputPassword
        )
    }

    fun saveBasicData(
        context: Context,
        doctor: Doctor
    ) {
        viewModelScope.launch {
            mSignInRepository.saveBasicData(
                context,
                doctor
            )
        }
    }
}