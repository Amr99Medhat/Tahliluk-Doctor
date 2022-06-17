package com.abdelrahman.amr.tahliluk_doctor.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.amr.tahliluk_doctor.R

import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivitySignInBinding
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.SignInViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import kotlinx.coroutines.flow.collect

class SignInActivity : AppCompatActivity() {
    private lateinit var mActivitySignInBinding: ActivitySignInBinding
    private lateinit var mSignInViewModel: SignInViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mActivitySignInBinding.root)
        mSignInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
       setListeners()
    }

    private fun setListeners() {
        mActivitySignInBinding.textSignUp.setOnClickListener {
            SupportClass.startActivity(this, SignUpActivity::class.java)
        }
        mActivitySignInBinding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
                SupportClass.loading(
                    true,
                    mActivitySignInBinding.btnSignIn,
                    mActivitySignInBinding.progressBar
                )
                lifecycleScope.launchWhenResumed {
                    signIn()
                }
            }
        }
        }

    private fun isValidSignInDetails(): Boolean {
        when {
            mActivitySignInBinding.doctorPhoneNumber.text.toString().trim().isEmpty() -> {
                mActivitySignInBinding.doctorPhoneNumber.error =
                    resources.getString(R.string.enter_phone_number)
                mActivitySignInBinding.doctorPhoneNumber.requestFocus()
                return false
            }
            mActivitySignInBinding.doctorPhoneNumber.text.toString().trim().length != 11 -> {
                mActivitySignInBinding.doctorPhoneNumber.error =
                    resources.getString(R.string.must_be_11_digits)
                mActivitySignInBinding.doctorPhoneNumber.requestFocus()
                return false
            }
            mActivitySignInBinding.doctorPassword.text.toString().trim().isEmpty() -> {
                mActivitySignInBinding.doctorPassword.error =
                    resources.getString(R.string.enter_password)
                mActivitySignInBinding.doctorPassword.requestFocus()
                return false
            }
            mActivitySignInBinding.doctorPassword.text.toString().trim().length < 7 -> {
                mActivitySignInBinding.doctorPassword.error =
                    resources.getString(R.string.less_than_7_digits)
                mActivitySignInBinding.doctorPassword.requestFocus()
                return false
            }
            else -> {
                return true
            }
        }
    }


    private suspend fun signIn() {
        mSignInViewModel.signIn(mActivitySignInBinding.doctorPhoneNumber.text.toString(),
        mActivitySignInBinding.doctorPassword.text.toString()
        ).collect {
            if (it.labId != Constants.KEY_EMPTY && it.labId != Constants.KEY_STRING_MINUS_ONE) {

                mSignInViewModel.saveBasicData(
                    this,
                    it
                )
                startMainActivityWithFlags()
            } else {
                SupportClass.loading(
                    false,
                    mActivitySignInBinding.btnSignIn,
                    mActivitySignInBinding.progressBar
                )


            }
            }
        }




    private fun unsuccessfulSignInMessage() {
        SupportClass.loading(
            false,
            mActivitySignInBinding.btnSignIn,
            mActivitySignInBinding.progressBar
        )
        SupportClass.showToast(
            applicationContext,
            resources.getString(R.string.unable_to_sign_in)
        )
    }

    private fun startMainActivityWithFlags() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }



    }
