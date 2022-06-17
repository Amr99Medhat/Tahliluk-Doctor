package com.abdelrahman.amr.tahliluk_doctor.viewModels

import androidx.lifecycle.ViewModel
import com.abdelrahman.amr.tahliluk_doctor.models.Lab
import com.abdelrahman.amr.tahliluk_doctor.repositories.LabsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class LabsViewModel:ViewModel() {

    private val mLabsRepository: LabsRepository = LabsRepository()

    suspend fun getLabs(): MutableStateFlow<ArrayList<Lab>> {
        return mLabsRepository.getLabs()
    }
}