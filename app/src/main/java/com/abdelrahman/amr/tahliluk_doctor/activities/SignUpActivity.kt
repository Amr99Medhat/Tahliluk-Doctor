package com.abdelrahman.amr.tahliluk_doctor.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivitySignUpBinding
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.SignUpViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.io.FileNotFoundException
import java.io.InputStream

class SignUpActivity : AppCompatActivity() {
    private lateinit var mActivitySignUpBinding: ActivitySignUpBinding
    private lateinit var mSignUpViewModel: SignUpViewModel
    private var mEncodedImage: String? = null
    private var mLabId: String? = null
    private var mLabName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mActivitySignUpBinding.root)
        mSignUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        setListeners()
    }

    private fun setListeners() {
        mActivitySignUpBinding.textSignIn.setOnClickListener {
            onBackPressed()
        }
        mActivitySignUpBinding.btnSignUp.setOnClickListener {
            if (isValidSignUpDetails()) {
                SupportClass.loading(
                    true,
                    mActivitySignUpBinding.btnSignUp,
                    mActivitySignUpBinding.progressBar
                )
                lifecycleScope.launchWhenResumed {
                    checkIfExistInLabs()
                }
            }
        }
        mActivitySignUpBinding.labImage.setOnClickListener {
            mSignUpViewModel.pickImage(resultLauncher)
        }
        mActivitySignUpBinding.workLab.setOnClickListener {
            mSignUpViewModel.getLocation(this, resultLauncher)
        }
    }

    private suspend fun checkIfExistInLabs() {
        mSignUpViewModel.checkIfExist(Constants.KEY_COLLECTION_LABS,
            Constants.KEY_LAB_PHONE_NUMBER,
            mActivitySignUpBinding.doctorPhoneNumber.text.toString()
        ).collect {
                patient ->
            when (patient) {
                Constants.KEY_TRUE_RETURN -> {
                    setNumberError()
                }
                Constants.KEY_FALSE_RETURN -> {
                    checkIfExistInPatients()
                }
            }
        }
    }
    private suspend fun checkIfExistInPatients() {
        mSignUpViewModel.checkIfExist(
            Constants.KEY_COLLECTION_PATIENTS,
            Constants.KEY_LAB_PHONE_NUMBER,
            mActivitySignUpBinding.doctorPhoneNumber.text.toString()
        ).collect { lab ->
            when (lab) {
                Constants.KEY_TRUE_RETURN -> {
                    setNumberError()
                }
                Constants.KEY_FALSE_RETURN -> {
                    signUp()
                }
            }
        }
    }

    private fun signUp() {
        SupportClass.loading(
            false,
            mActivitySignUpBinding.btnSignUp,
            mActivitySignUpBinding.progressBar
        )
        goForVerification()

    }
    private fun goForVerification() {
        val intent = Intent(this, VerifyPhoneNumberActivity::class.java)
        intent.putExtra(Constants.KEY_DOCTOR_IMAGE, mEncodedImage!!)
        intent.putExtra(Constants.KEY_DOCTOR_FIRST_NAME, mActivitySignUpBinding.etDoctorFirstName.text.toString())
        intent.putExtra(
            Constants.KEY_DOCTOR_PHONE_NUMBER,
            mActivitySignUpBinding.doctorPhoneNumber.text.toString()
        )
        intent.putExtra(Constants.KEY_DOCTOR_LAST_NAME, mActivitySignUpBinding.etDoctorLastName.text.toString())
        intent.putExtra(Constants.KEY_DOCTOR_PASSWORD, mActivitySignUpBinding.doctorPassword.text.toString())
        intent.putExtra(Constants.KEY_DOCTOR_LAB_ID, mLabId)
        intent.putExtra(Constants.KEY_LUNCH_STATE, Constants.KEY_LUNCH_STATE_FIRST_TIME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun isValidSignUpDetails(): Boolean {
        if (mEncodedImage == null) {
            SupportClass.showToast(
                applicationContext,
                resources.getString(R.string.select_profile_image)
            )
            return false
        } else if (mActivitySignUpBinding.etDoctorFirstName.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.etDoctorFirstName.error = resources.getString(R.string.enter_first_name)
            mActivitySignUpBinding.etDoctorFirstName.requestFocus()
            return false
        }else if (mActivitySignUpBinding.etDoctorLastName.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.etDoctorLastName.error = resources.getString(R.string.enter_last_name)
            mActivitySignUpBinding.etDoctorLastName.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorPhoneNumber.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.doctorPhoneNumber.error =
                resources.getString(R.string.enter_phone_number)
            mActivitySignUpBinding.doctorPhoneNumber.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorPhoneNumber.text.toString().trim().length != 11) {
            mActivitySignUpBinding.doctorPhoneNumber.error =
                resources.getString(R.string.must_be_11_digits)
            mActivitySignUpBinding.doctorPhoneNumber.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorPassword.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.doctorPassword.error = resources.getString(R.string.enter_password)
            mActivitySignUpBinding.doctorPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorPassword.text.toString().trim().length < 7) {
            mActivitySignUpBinding.doctorPassword.error =
                resources.getString(R.string.less_than_7_digits)
            mActivitySignUpBinding.doctorPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorConfirmPassword.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.doctorConfirmPassword.error =
                resources.getString(R.string.enter_confirm_password)
            mActivitySignUpBinding.doctorConfirmPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.doctorPassword.text.toString() != mActivitySignUpBinding.doctorConfirmPassword.text.toString()) {
            mActivitySignUpBinding.doctorConfirmPassword.error =
                resources.getString(R.string.not_the_same_password)
            return false
        } else if (mActivitySignUpBinding.workLab.text == resources.getString(R.string.choose_lab)) {
            mActivitySignUpBinding.workLab.error =
                resources.getString(R.string.choose_lab)
            SupportClass.showToast(
                applicationContext,
                resources.getString(R.string.choose_lab)
            )
            return false
        } else {
            return true
        }
    }

    private fun setNumberError() {
        mActivitySignUpBinding.doctorPhoneNumber.error =
            resources.getString(R.string.this_phone_number_already_have_an_account)
        mActivitySignUpBinding.doctorPhoneNumber.requestFocus()
        SupportClass.loading(
            false,
            mActivitySignUpBinding.btnSignUp,
            mActivitySignUpBinding.progressBar
        )
    }

    @SuppressLint("SetTextI18n")
    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                when (result.resultCode) {
                    RESULT_OK -> {
                        val imageUri: Uri? = result.data!!.data
                        try {
                            val inputStream: InputStream? =
                                contentResolver.openInputStream(imageUri!!)
                            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                            mActivitySignUpBinding.labImage.setImageBitmap(bitmap)
                            mEncodedImage = SupportClass.encodedImage(bitmap)

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    Constants.KEY_Lab_RESULT_CODE -> {
                        val intent = result.data
                        if (intent != null) {
                            val labName =
                                intent.getStringExtra(Constants.KEY_LAB_NAME_RESULT)
                            mLabName = labName
                            val labId =
                                intent.getStringExtra(Constants.KEY_LAB_ID_RESULT)
                            mLabId = labId

                            mActivitySignUpBinding.workLab.text =
                                labName
                        }
                    }
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mSignUpViewModel.viewModelScope.cancel()
    }
}