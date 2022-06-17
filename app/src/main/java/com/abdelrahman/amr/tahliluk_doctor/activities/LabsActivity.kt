package com.abdelrahman.amr.tahliluk_doctor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.amr.tahliluk_doctor.adapters.LabsAdapter
import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivityLabsBinding
import com.abdelrahman.amr.tahliluk_doctor.listeners.OnLabClickListener
import com.abdelrahman.amr.tahliluk_doctor.models.Lab
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.LabsViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class LabsActivity : AppCompatActivity(),OnLabClickListener {
    private lateinit var mLabsBinding: ActivityLabsBinding
    private lateinit var mLabsViewModel: LabsViewModel
    private lateinit var mLabsList: ArrayList<Lab>
    private lateinit var mLabsListAdapter: LabsAdapter
    private var parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLabsBinding = ActivityLabsBinding.inflate(layoutInflater)
        setContentView(mLabsBinding.root)
        mLabsViewModel = ViewModelProvider(this)[LabsViewModel::class.java]
        getLabs()

    }
    private fun getLabs() {
       lifecycleScope.launchWhenResumed {
           mLabsViewModel.getLabs().collect {
               if (it.isNotEmpty()){
                   mLabsList=ArrayList()
                   mLabsList = it
                   mLabsBinding.LabsRecyclerView.visibility = View.VISIBLE
                   mLabsBinding.tvNoLabs.visibility=View.GONE
                   SupportClass.loading(false, null, mLabsBinding.progressBar)
                   mLabsListAdapter = LabsAdapter(mLabsList,this@LabsActivity)
                   mLabsBinding.LabsRecyclerView.adapter = mLabsListAdapter

               }
//               coroutineScope.launch {
//                   delay(1000)
//                   if(mLabsList.isEmpty())
//                   {
//                       mLabsBinding.tvNoLabs.visibility=View.VISIBLE
//                       SupportClass.loading(false,
//                           null,
//                           mLabsBinding.progressBar)
//                   }
//               }
           }
       }
    }
    override fun onLabClickListener(lab: Lab) {
        val intent = Intent()
        intent.putExtra(Constants.KEY_LAB_ID, lab.labId)
        intent.putExtra(Constants.KEY_LAB_NAME_RESULT,lab.labName)
        setResult(Constants.KEY_Lab_RESULT_CODE, intent)
        onBackPressed()
    }
}