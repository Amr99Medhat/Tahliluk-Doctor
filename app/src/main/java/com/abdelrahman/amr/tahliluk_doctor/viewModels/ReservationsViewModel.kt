package com.abdelrahman.amr.tahliluk_doctor.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass
import com.abdelrahman.amr.tahliluk_doctor.models.Patient
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.repositories.ReservationDetailsRepository
import com.abdelrahman.amr.tahliluk_doctor.repositories.ReservationsRepository
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class ReservationsViewModel:ViewModel() {
    private val mReservationsRepository: ReservationsRepository = ReservationsRepository()

     suspend fun getReservations(orderDate:String,context: Context): MutableStateFlow<ArrayList<Reserve>> {
        return mReservationsRepository.getReservations(orderDate,context)
    }

    suspend fun getPatientInfo(patientId:String):MutableStateFlow<Patient>{
        return  mReservationsRepository.getPatientInfo(Constants.KEY_COLLECTION_PATIENTS,patientId)
    }

}