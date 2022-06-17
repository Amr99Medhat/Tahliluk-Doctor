package com.abdelrahman.amr.tahliluk_doctor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivityVerifyPhoneNumberBinding
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.VerifyPhoneNumberViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.util.HashMap

class VerifyPhoneNumberActivity : AppCompatActivity() {
    private lateinit var mActivityVerifyPhoneNumberBinding: ActivityVerifyPhoneNumberBinding
    private lateinit var mVerifyPhoneNumberViewModel: VerifyPhoneNumberViewModel
    private lateinit var mAuth: FirebaseAuth
    private var mVerificationCodeBySystem: String? = null
    private var mDoctorImage: String? = null
    private var mDoctorFirstName: String? = null
    private var mDoctorLastName: String? = null
    private var mDoctorPhoneNumber: String? = null
    private var mDoctorPassword: String? = null
    private var mDoctorLab: String? = null
    private var mLunchState: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityVerifyPhoneNumberBinding = ActivityVerifyPhoneNumberBinding.inflate(layoutInflater)
        setContentView(mActivityVerifyPhoneNumberBinding.root)
        mVerifyPhoneNumberViewModel = ViewModelProvider(this)[VerifyPhoneNumberViewModel::class.java]
        mAuth = Firebase.auth
        getDataFromIntent()
        setListeners()
    }

    private fun setListeners() {
        mActivityVerifyPhoneNumberBinding.btnVerify.setOnClickListener {
            val code = mActivityVerifyPhoneNumberBinding.codeInputView.code
            if (code.isEmpty() || code.length < 6) {
                mActivityVerifyPhoneNumberBinding.codeInputView.error =
                    resources.getString(R.string.wrong_otp)
                mActivityVerifyPhoneNumberBinding.codeInputView.requestFocus()
                return@setOnClickListener
            }
            SupportClass.loading(true, null, mActivityVerifyPhoneNumberBinding.progressBar)
            verifyCode(code)

        }
        mActivityVerifyPhoneNumberBinding.refreshCodeInput.setOnClickListener {
            refreshCodeInput()
        }
    }


    private fun getDataFromIntent() {
        mDoctorImage = intent.getStringExtra(Constants.KEY_DOCTOR_IMAGE)
        mDoctorFirstName = intent.getStringExtra(Constants.KEY_DOCTOR_FIRST_NAME)
        mDoctorLastName = intent.getStringExtra(Constants.KEY_DOCTOR_LAST_NAME)
        mDoctorPhoneNumber = intent.getStringExtra(Constants.KEY_DOCTOR_PHONE_NUMBER)
        mDoctorPassword = intent.getStringExtra(Constants.KEY_DOCTOR_PASSWORD)
        mDoctorLab=intent.getStringExtra(Constants.KEY_DOCTOR_LAB_ID)
        mLunchState = intent.getStringExtra(Constants.KEY_LUNCH_STATE)

        //TODO("I Disabled the verification fun to test and will enable it")
        //verifyPhoneNumberViewModel.sendVerificationCodeToLab(this, labPhoneNumber!!, mCallbacks)
        lifecycleScope.launchWhenResumed {
            signUp()
        }
    }




    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        //if the sim card not in the device
        override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, p1)
            mVerificationCodeBySystem = s
        }

        //if the sim card in the device
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                SupportClass.loading(
                    true,
                    mActivityVerifyPhoneNumberBinding.btnVerify,
                    mActivityVerifyPhoneNumberBinding.progressBar
                )
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            SupportClass.showToast(applicationContext, e.message!!.toString())
        }
    }

    private fun verifyCode(codeByUser: String) {
        if (mVerificationCodeBySystem != null) {
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(mVerificationCodeBySystem!!, codeByUser)
            lifecycleScope.launchWhenResumed {
                signInByCredentials(credential)
            }
        } else {
            SupportClass.showToast(applicationContext, resources.getString(R.string.wrong_code))
        }
    }

    private suspend fun signInByCredentials(credential: PhoneAuthCredential) {
     mVerifyPhoneNumberViewModel.signInByCredentials(this,credential).collect {signInByCredentialsResult ->
         when (signInByCredentialsResult) {
             Constants.KEY_EMPTY -> {}
             Constants.KEY_TRUE_RETURN -> {
                 if (mLunchState == Constants.KEY_LUNCH_STATE_FIRST_TIME) {
                     signUp()
                 } else if (mLunchState == Constants.KEY_LUNCH_STATE_FORGOT_PASSWORD) {
                     //TODO("goToResetPassword")
                     //goToResetPassword()
                     Log.d("here", "goToResetPassword()")
                 }
             }

             else -> {
                 SupportClass.showToast(
                     applicationContext,
                     signInByCredentialsResult
                 )
             }
         }

     }

    }



    private suspend fun signUp() {
        SupportClass.loading(
            true,
            mActivityVerifyPhoneNumberBinding.btnVerify,
            mActivityVerifyPhoneNumberBinding.progressBar
        )
        val doctor: HashMap<Any, Any> = HashMap()
        doctor[Constants.KEY_DOCTOR_FIRST_NAME] = mDoctorFirstName!!
        doctor[Constants.KEY_DOCTOR_LAST_NAME] = mDoctorLastName!!
        doctor[Constants.KEY_DOCTOR_IMAGE] = mDoctorImage!!
        doctor[Constants.KEY_DOCTOR_PHONE_NUMBER] = mDoctorPhoneNumber!!
        doctor[Constants.KEY_PASSWORD] = mDoctorPassword!!
        doctor[Constants.KEY_DOCTOR_LAB_ID] = mDoctorLab!!


        mVerifyPhoneNumberViewModel.signUp(doctor).collect { signUpResultId ->

            when (signUpResultId[Constants.KEY_FALSE_RETURN]) {
                Constants.KEY_FALSE_RETURN -> {
                    SupportClass.loading(
                        false,
                        mActivityVerifyPhoneNumberBinding.btnVerify,
                        mActivityVerifyPhoneNumberBinding.progressBar
                    )

                    startSignInActivityWithFlags()
                }
                Constants.KEY_TRUE_RETURN -> {
                    SupportClass.loading(
                        false,
                        mActivityVerifyPhoneNumberBinding.btnVerify,
                        mActivityVerifyPhoneNumberBinding.progressBar
                    )
                    SupportClass.showToast(
                        applicationContext,
                        signUpResultId[Constants.KEY_DATA]!!.toString()
                    )
                }
            }
        }
    }





    private fun refreshCodeInput() {
        mActivityVerifyPhoneNumberBinding.codeInputView.error = Constants.KEY_EMPTY
        mActivityVerifyPhoneNumberBinding.codeInputView.clearError()
        mActivityVerifyPhoneNumberBinding.codeInputView.code = Constants.KEY_EMPTY
        mActivityVerifyPhoneNumberBinding.codeInputView.setEditable(true)
    }

    private fun startSignInActivityWithFlags() {
        val intent = Intent(applicationContext, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mVerifyPhoneNumberViewModel.viewModelScope.cancel()
    }
}