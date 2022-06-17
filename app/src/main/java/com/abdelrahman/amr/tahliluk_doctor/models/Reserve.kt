package com.abdelrahman.amr.tahliluk_doctor.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

class Reserve() : Parcelable {
    var orderId:String?=null
    var patientId:String? = null
    var labId:String?=null
    var orderLocation:GeoPoint?=null
    var orderAddress:String?=null
    var analyticsList:ArrayList<Analytics>?=null
    var results :String?=null
    var orderState:String?=null
    var orderUserPhone:String?=null
    var orderDateTime:String?=null
    var orderAnalyticsPrice:String? = null
    var orderTotalAmount:String? = null
    var orderAdditionalInformation:String?=null

    constructor(parcel: Parcel) : this() {
        orderId = parcel.readString()
        patientId = parcel.readString()
        labId = parcel.readString()
        orderAddress = parcel.readString()
        results = parcel.readString()
        orderState = parcel.readString()
        orderUserPhone = parcel.readString()
        orderDateTime = parcel.readString()
        orderAnalyticsPrice = parcel.readString()
        orderTotalAmount = parcel.readString()
        orderAdditionalInformation = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(patientId)
        parcel.writeString(labId)
        parcel.writeString(orderAddress)
        parcel.writeString(results)
        parcel.writeString(orderState)
        parcel.writeString(orderUserPhone)
        parcel.writeString(orderDateTime)
        parcel.writeString(orderAnalyticsPrice)
        parcel.writeString(orderTotalAmount)
        parcel.writeString(orderAdditionalInformation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reserve> {
        override fun createFromParcel(parcel: Parcel): Reserve {
            return Reserve(parcel)
        }

        override fun newArray(size: Int): Array<Reserve?> {
            return arrayOfNulls(size)
        }
    }


}