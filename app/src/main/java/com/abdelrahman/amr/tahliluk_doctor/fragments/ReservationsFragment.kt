package com.abdelrahman.amr.tahliluk_doctor.fragments

import android.Manifest
import android.annotation.SuppressLint

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.adapters.ReservationsAdapter
import com.abdelrahman.amr.tahliluk_doctor.databinding.DialogProgressBinding
import com.abdelrahman.amr.tahliluk_doctor.databinding.FragmentReservationsBinding
import com.abdelrahman.amr.tahliluk_doctor.listeners.ReservationListener
import com.abdelrahman.amr.tahliluk_doctor.models.Patient
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.ReservationsViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReservationsFragment : Fragment(), DatePickerListener, ReservationListener {
    private lateinit var mReservationFragment: FragmentReservationsBinding
    private lateinit var mReservationsViewModel: ReservationsViewModel
    private lateinit var mReservationsListAdapter: ReservationsAdapter
    private var mReservationsList: ArrayList<Reserve> = ArrayList()
    private lateinit var bindingDialog: DialogProgressBinding
    private var parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    private var mCurrentLatLong: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReservationsViewModel = ViewModelProvider(this)[ReservationsViewModel::class.java]
        checkPermission()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mReservationFragment = FragmentReservationsBinding.inflate(layoutInflater)
        bindingDialog = DialogProgressBinding.inflate(layoutInflater)
        setDatePicker()

        getCurrentLocation()

        return mReservationFragment.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReservationsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun setDatePicker() {
        mReservationFragment.calendarView.setListener(this)
            .setDateSelectedColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDark
                )
            )
            .setDateSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setDayOfWeekTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            ) // لون الايام
            .setDateSelectedColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.day_background
                )
            ) // لون خلفية اليوم اللي هحدده
            .setUnselectedDayTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setMonthAndYearTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setTodayDateBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setTodayButtonTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            .init()
        mReservationFragment.calendarView.setDate(DateTime())
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        if (dateSelected != null) {
            calendar[dateSelected.year, dateSelected.monthOfYear - 1] = dateSelected!!.dayOfMonth
        }
        val dateString = sdf.format(calendar.time)
        mReservationsList = ArrayList()
        if (mReservationsList.isNotEmpty()) {
            mReservationsList.clear()
        }
        getReservations(dateString)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getReservations(orderDate: String) {

        lifecycleScope.launchWhenResumed {

            mReservationsViewModel.getReservations(orderDate, requireContext()).collect {
                Log.d("size", it.size.toString())
                Log.d("date", orderDate)


                if (it.isEmpty()) {
                    mReservationFragment.progressBar.visibility = View.GONE
                    mReservationFragment.rvReservations.visibility = View.GONE
                    mReservationFragment.tvNoReservations.visibility = View.VISIBLE
                } else {
                    mReservationsList = it
                    SupportClass.loading(false, null, mReservationFragment.progressBar)
                    mReservationFragment.rvReservations.visibility = View.VISIBLE
                    mReservationFragment.tvNoReservations.visibility = View.GONE
                    mReservationsListAdapter = ReservationsAdapter(
                        mReservationsList, this@ReservationsFragment
                    )
                    mReservationFragment.rvReservations.adapter =
                        mReservationsListAdapter

                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    activity?.let {
                        LocationServices.getFusedLocationProviderClient(it)
                            .removeLocationUpdates(this)
                    }
                    if (locationResult.locations.size > 0) {
                        val index = locationResult.locations.size - 1
                        val latitude = locationResult.locations[index].latitude
                        val longitude = locationResult.locations[index].longitude
                        mCurrentLatLong = LatLng(latitude, longitude)
                    }
                }
            }, Looper.getMainLooper())
    }


    override fun onReservationClickListener(reserve: Reserve) {
        SupportClass.showProgressBar(
            requireContext(),
            resources.getString(R.string.please_wait),
            bindingDialog.tvProgressText
        )
        var patient = Patient()
        lifecycleScope.launchWhenResumed {
            mReservationsViewModel.getPatientInfo(reserve.patientId!!).collect {
                patient = it

            }
        }
        coroutineScope.launch {
            delay(500)
            val reservationDetailsFragment = ReservationsDetailsFragment.newInstance()
            val bundle = Bundle()
            bundle.putParcelable(Constants.Reservation, reserve)
            bundle.putString(Constants.KEY_PATIENT_FIRST_NAME, patient.firstName)
            Log.d("name", patient.firstName)
            bundle.putString(Constants.KEY_PATIENT_LAST_NAME, patient.lastName)
            bundle.putString(Constants.KEY_PATIENT_IMAGE, patient.image)
            reservationDetailsFragment.arguments = bundle
            val fragmentManager: FragmentManager =
                (mReservationFragment.root.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation,
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation
            )
            fragmentTransaction.replace(R.id.frameLayout, reservationDetailsFragment)
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.commit()
            SupportClass.hideDialog()

        }

    }


    override fun onCallClickListener(number: String) {
        val phoneIntent = Intent(Intent.ACTION_CALL)
        phoneIntent.data = Uri.parse("tel:$number")
        startActivity(phoneIntent)

    }

    override fun onShowOnMapClickListener(latlan: GeoPoint) {
        val uri =
            Uri.parse("https://www.google.co.in/maps/dir/${mCurrentLatLong!!.latitude},${mCurrentLatLong!!.longitude}/${latlan.latitude},${latlan.longitude}")
        val dirIntent = Intent(Intent.ACTION_VIEW, uri)
        dirIntent.setPackage("com.google.android.apps.maps")
        startActivity(dirIntent)

    }

    private fun checkPermission(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    getCurrentLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    getCurrentLocation()
                } else -> {
                // No location access granted.
            }
            }
        }

        if ( ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation()
        }
        else{
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE))
        }
    }


}
