package com.abdelrahman.amr.tahliluk_doctor.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.adapters.InProgressReservationsAdapter
import com.abdelrahman.amr.tahliluk_doctor.databinding.FragmentInProgressReservationsBinding
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.viewModels.CompletedReservationsViewModel

import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import kotlinx.coroutines.flow.collect
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InProgressReservationsFragment : Fragment(),DatePickerListener {
    private lateinit var mInProgressReservationFragment: FragmentInProgressReservationsBinding
    private lateinit var mReservationsListAdapter: InProgressReservationsAdapter
    private  var mReservationsList: ArrayList<Reserve> = ArrayList()
    private lateinit var minProgressReservationsViewModel:CompletedReservationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        minProgressReservationsViewModel = ViewModelProvider(this)[CompletedReservationsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mInProgressReservationFragment = FragmentInProgressReservationsBinding.inflate(layoutInflater)
        setDatePicker()
        return mInProgressReservationFragment.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InProgressReservationsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun setDatePicker() {
        mInProgressReservationFragment.calendarView.setListener(this)
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
        mInProgressReservationFragment.calendarView.setDate(DateTime())
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        if (dateSelected != null) {
            calendar[dateSelected.year, dateSelected.monthOfYear - 1] = dateSelected.dayOfMonth
        }
        val dateString = sdf.format(calendar.time)
        mReservationsList = ArrayList()
        if (mReservationsList.isNotEmpty()){
            mReservationsList.clear()
        }
        getReservations(dateString)

    }

    private fun getReservations(orderDate: String) {


        lifecycleScope.launchWhenResumed{

            minProgressReservationsViewModel.getReservations(orderDate,requireContext()).collect {
                Log.d("size",it.size.toString())
                Log.d("date",orderDate)


                if (it.isEmpty()) {
                    mInProgressReservationFragment.progressBar.visibility = View.GONE
                    mInProgressReservationFragment.rvReservations.visibility = View.GONE
                    mInProgressReservationFragment.tvNoReservations.visibility = View.VISIBLE
                }
                else{
                    mReservationsList=it
                    SupportClass.loading(false, null, mInProgressReservationFragment.progressBar)
                    mInProgressReservationFragment.rvReservations.visibility = View.VISIBLE
                    mInProgressReservationFragment.tvNoReservations.visibility = View.GONE
                    mReservationsListAdapter = InProgressReservationsAdapter(
                        mReservationsList)
                    mInProgressReservationFragment.rvReservations.adapter =
                        mReservationsListAdapter

                }
            }
        }

    }
}