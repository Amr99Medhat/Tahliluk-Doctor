package com.abdelrahman.amr.tahliluk_doctor.repositories

import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.models.Lab
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class LabsRepository {
    private var mLabsArrayList: MutableStateFlow<ArrayList<Lab>> =
        MutableStateFlow(ArrayList())

    suspend fun getLabs(): MutableStateFlow<ArrayList<Lab>> {
        mLabsArrayList = MutableStateFlow(ArrayList())
        FirestoreClass().getLabs(Constants.KEY_COLLECTION_LABS).addOnSuccessListener {
            val labList: ArrayList<Lab> = ArrayList()
            for (labObject in it.documents) {
                val lab = labObject.toObject(Lab::class.java)
                if (lab != null) {
                    lab.labId = labObject.id
                    labList.add(lab)
                }
            }
            runBlocking { mLabsArrayList.emit(labList) }
        }
        return mLabsArrayList
    }
}