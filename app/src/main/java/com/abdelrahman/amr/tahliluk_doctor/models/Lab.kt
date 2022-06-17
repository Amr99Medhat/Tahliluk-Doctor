package com.abdelrahman.amr.tahliluk_doctor.models

import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import java.io.Serializable

class Lab:Serializable {
    var labId: String = Constants.KEY_EMPTY
    var image: String = Constants.KEY_EMPTY
    var labName: String = Constants.KEY_EMPTY

}