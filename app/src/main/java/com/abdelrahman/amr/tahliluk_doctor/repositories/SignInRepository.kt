package com.abdelrahman.amr.tahliluk_doctor.repositories

import android.content.Context
import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.models.Doctor
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class SignInRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mSignInResult: MutableStateFlow<Doctor> = MutableStateFlow(Doctor())
    private var mDoctor: Doctor = Doctor()

    suspend fun signIn(
        inputNumber: String,
        inputPassword: String
    ): MutableStateFlow<Doctor> {
        val task = FirestoreClass().signIn(
            Constants.KEY_COLLECTION_Doctors,
            Constants.KEY_DOCTOR_PHONE_NUMBER,
            inputNumber,
            Constants.KEY_PASSWORD,
            inputPassword
        )
        mSignInResult = MutableStateFlow(Doctor())
        mDoctor = Doctor()
        task.addOnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                runBlocking {
                    val documentSnapshot = it.result!!.documents[0]
                    mDoctor.doctorId = documentSnapshot.id
                    mDoctor.firstName = documentSnapshot.getString(Constants.KEY_DOCTOR_FIRST_NAME)!!
                    mDoctor.lastName = documentSnapshot.getString(Constants.KEY_DOCTOR_LAST_NAME)!!
                    mDoctor.image = documentSnapshot.getString(Constants.KEY_DOCTOR_IMAGE)!!
                    mDoctor.phoneNumber =
                        documentSnapshot.getString(Constants.KEY_DOCTOR_PHONE_NUMBER)!!
                    mDoctor.password = documentSnapshot.getString(Constants.KEY_DOCTOR_PASSWORD)!!
                    mDoctor.labId = documentSnapshot.getString(Constants.KEY_DOCTOR_LAB_ID)!!

                    mSignInResult.emit(mDoctor)
                }

            } else {
                runBlocking {
                    mDoctor.labId = "-1"
                    mDoctor.firstName = "-1"
                    mDoctor.lastName = "-1"
                    mDoctor.image = "-1"
                    mDoctor.phoneNumber = "-1"
                    mDoctor.password = "-1"
                    mDoctor.labId = "-1"


                    mSignInResult.emit(mDoctor)
                }
            }
        }
        return mSignInResult
    }


    fun saveBasicData(
        context: Context,
        doctor: Doctor
    ) {
        mPreferenceManager = PreferenceManager(context)
        mPreferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
        mPreferenceManager.putString(Constants.KEY_DOCTOR_ID, doctor.doctorId)
        mPreferenceManager.putString(Constants.KEY_IMAGE, doctor.image)
        mPreferenceManager.putString(Constants.KEY_DOCTOR_FIRST_NAME, doctor.firstName)
        mPreferenceManager.putString(Constants.KEY_DOCTOR_PHONE_NUMBER, doctor.phoneNumber)
        mPreferenceManager.putString(Constants.KEY_DOCTOR_PASSWORD, doctor.password)
        mPreferenceManager.putString(Constants.KEY_DOCTOR_LAB_ID, doctor.labId)

    }

}