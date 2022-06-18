package com.abdelrahman.amr.tahliluk_doctor.repositories

import android.content.Context
import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.models.Patient
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class CompletedReservationsRepository {
    private var mReservationsArrayList: MutableStateFlow<ArrayList<Reserve>> =
        MutableStateFlow(ArrayList())
    private lateinit var mPreferenceManager: PreferenceManager


    suspend fun getReservations( orderDate:String,context: Context): MutableStateFlow<ArrayList<Reserve>> {
        mPreferenceManager = PreferenceManager(context)
        val labId=mPreferenceManager.getString(Constants.KEY_DOCTOR_LAB_ID)!!
        FirestoreClass().getReservations(
            Constants.KEY_COLLECTION_RESERVATION,labId,orderDate,
            Constants.ORDER_STATE_iN_PROGRESS).addOnSuccessListener {
            val reservationsList: ArrayList<Reserve> = ArrayList()
            for (labObject in it.documents) {
                val reservation = labObject.toObject(Reserve::class.java)
                if (reservation != null) {
                    reservation.orderId = labObject.id
                    reservationsList.add(reservation)
                }
            }
            runBlocking { mReservationsArrayList.emit(reservationsList) }
        }
        return mReservationsArrayList
    }

    private var mPatient: MutableStateFlow<Patient> =
        MutableStateFlow(Patient())

    suspend fun getPatientInfo(collectionName:String,patientId: String):MutableStateFlow<Patient>{
        FirestoreClass().getPatientInfo(collectionName,patientId).addOnSuccessListener {
            val patient= Patient()
            patient.firstName = it.get(Constants.KEY_PATIENT_FIRST_NAME) as String
            patient.lastName = it.get(Constants.KEY_PATIENT_LAST_NAME) as String
            patient.image = it.get(Constants.KEY_PATIENT_IMAGE) as String

            runBlocking { mPatient.emit(patient) }
        }
        return mPatient
    }
}