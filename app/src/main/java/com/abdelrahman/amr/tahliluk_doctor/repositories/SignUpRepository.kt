package com.abdelrahman.amr.tahliluk_doctor.repositories

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import com.abdelrahman.amr.tahliluk_doctor.activities.LabsActivity
import com.abdelrahman.amr.tahliluk_doctor.activities.SignUpActivity
import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class SignUpRepository {
    private var mNumberExistState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    fun pickImage(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        resultLauncher.launch(intent)
    }

    fun getLab(activity: SignUpActivity, resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(activity, LabsActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun checkIfExist(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String
    ): MutableStateFlow<String> {
        mNumberExistState = MutableStateFlow(Constants.KEY_EMPTY)
        val task = FirestoreClass().checkIfExist(collectionName, KeyNumber, inputNumber)
        task.addOnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                runBlocking { mNumberExistState.emit(Constants.KEY_TRUE_RETURN) }
            } else {
                runBlocking { mNumberExistState.emit(Constants.KEY_FALSE_RETURN) }
            }
        }
        return mNumberExistState
    }
}