package com.abdelrahman.amr.tahliluk_doctor.repositories

import com.abdelrahman.amr.tahliluk_doctor.firebase.FirestoreClass



class ReservationDetailsRepository {
    suspend fun updateReservation(collectionName:String,reservationId:String,reserveMap:HashMap<String,Any>){

        FirestoreClass().updateReservation(collectionName,reservationId,reserveMap)
    }
}