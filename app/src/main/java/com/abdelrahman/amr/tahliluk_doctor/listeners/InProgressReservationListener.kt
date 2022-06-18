package com.abdelrahman.amr.tahliluk_doctor.listeners

import com.abdelrahman.amr.tahliluk_doctor.models.Reserve

interface InProgressReservationListener {
    fun onReservationClickListener(reserve: Reserve)
}