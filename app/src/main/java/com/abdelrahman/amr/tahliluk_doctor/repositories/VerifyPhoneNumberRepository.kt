package com.abdelrahman.amr.tahliluk_doctor.repositories

import com.abdelrahman.amr.tahliluk_doctor.activities.VerifyPhoneNumberActivity
import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyPhoneNumberRepository {
    private var mAuth: FirebaseAuth = Firebase.auth
    private var mSignInByCredentialsResult: MutableStateFlow<String> =
        MutableStateFlow(Constants.KEY_EMPTY)
    private var mSuccessSignUp: MutableStateFlow<HashMap<String, String>> =
        MutableStateFlow(HashMap())
    private var mData: HashMap<String, String> = HashMap()

    fun sendVerificationCodeToLab(
        activity: VerifyPhoneNumberActivity,
        phoneNo: String,
        mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+20$phoneNo")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun signInByCredentials(
        activity: VerifyPhoneNumberActivity,
        credential: PhoneAuthCredential
    ): MutableStateFlow<String> {
        mSignInByCredentialsResult = MutableStateFlow(Constants.KEY_EMPTY)
        val authResult = FirestoreClass().signInByCredentials(credential)
        authResult.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                runBlocking { mSignInByCredentialsResult.emit(Constants.KEY_TRUE_RETURN) }
            } else {
                runBlocking { mSignInByCredentialsResult.emit(task.exception!!.message.toString()) }
            }
        }
        authResult.addOnFailureListener {
            runBlocking { mSignInByCredentialsResult.emit(it.message.toString()) }
        }
        return mSignInByCredentialsResult
    }

    suspend fun signUp(lab: HashMap<Any, Any>): MutableStateFlow<HashMap<String, String>> {
        mSuccessSignUp = MutableStateFlow(HashMap())
        mData = HashMap()
        val task = FirestoreClass().signUp(Constants.KEY_COLLECTION_Doctors, lab)
        task.addOnSuccessListener {
            mData[Constants.KEY_FALSE_RETURN] = Constants.KEY_FALSE_RETURN
            mData[Constants.KEY_DATA] = it.id
            runBlocking { mSuccessSignUp.emit(mData) }
        }
        task.addOnFailureListener {
            mData[Constants.KEY_FALSE_RETURN] = Constants.KEY_TRUE_RETURN
            mData[Constants.KEY_DATA] = it.message.toString()
            runBlocking { mSuccessSignUp.emit(mData) }
        }
        return mSuccessSignUp
    }
}