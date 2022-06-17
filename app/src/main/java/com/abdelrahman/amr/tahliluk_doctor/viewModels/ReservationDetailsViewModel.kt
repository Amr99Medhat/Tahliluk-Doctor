package com.abdelrahman.amr.tahliluk_doctor.viewModels

import androidx.lifecycle.ViewModel
import com.abdelrahman.amr.tahliluk_doctor.models.Patient
import com.abdelrahman.amr.tahliluk_doctor.repositories.ReservationDetailsRepository
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow

class ReservationDetailsViewModel:ViewModel() {
 private var mReservationDetailsRepository:ReservationDetailsRepository = ReservationDetailsRepository()

    suspend fun updateReservation(reservationId:String,reserveMap:HashMap<String,Any>){
        mReservationDetailsRepository.updateReservation(Constants.KEY_COLLECTION_RESERVATION,reservationId,reserveMap)
    }
}