package com.abdelrahman.amr.tahliluk_doctor.models

import android.provider.SyncStateContract
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import java.io.Serializable

class Doctor :Serializable {
    var doctorId:String=Constants.KEY_EMPTY
    var firstName:String = Constants.KEY_EMPTY
    var lastName:String=Constants.KEY_EMPTY
    var image:String=Constants.KEY_EMPTY
    var phoneNumber:String=Constants.KEY_EMPTY
    var password:String=Constants.KEY_EMPTY
    var labId:String=Constants.KEY_EMPTY

}