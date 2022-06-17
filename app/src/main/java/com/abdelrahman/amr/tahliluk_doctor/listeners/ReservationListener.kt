package com.abdelrahman.amr.tahliluk_doctor.listeners

import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint


interface ReservationListener {
    fun onReservationClickListener(reserve: Reserve)
    fun onCallClickListener(number:String)
    fun onShowOnMapClickListener(latlan:GeoPoint)

}