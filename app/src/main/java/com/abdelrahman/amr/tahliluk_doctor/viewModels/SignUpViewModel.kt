package com.abdelrahman.amr.tahliluk_doctor.viewModels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.amr.tahliluk_doctor.activities.SignUpActivity
import com.abdelrahman.amr.tahliluk_doctor.repositories.SignUpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private var mSignUpRepository: SignUpRepository = SignUpRepository()

    fun pickImage(resultLauncher: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            mSignUpRepository.pickImage(resultLauncher)
        }
    }

    fun getLocation(activity: SignUpActivity, resultLauncher: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            mSignUpRepository.getLab(activity, resultLauncher)
        }
    }

    fun checkIfExist(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String
    ): MutableStateFlow<String> {
        return mSignUpRepository.checkIfExist(
            collectionName,
            KeyNumber,
            inputNumber
        )
    }

}