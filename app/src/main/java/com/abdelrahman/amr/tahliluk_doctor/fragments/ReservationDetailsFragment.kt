package com.abdelrahman.amr.tahliluk_doctor.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.adapters.AnalyticsAdapter
import com.abdelrahman.amr.tahliluk_doctor.databinding.FragmentReservationDetailsBinding
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.abdelrahman.amr.tahliluk_doctor.viewModels.ReservationDetailsViewModel

class ReservationsDetailsFragment : Fragment() {
    private lateinit var mPatientName :String
    private lateinit var mPatientImage :String
    private lateinit var mReservationDetailsViewModel:ReservationDetailsViewModel
    private lateinit var reservationDetailsBinding: FragmentReservationDetailsBinding
    private lateinit var reservation: Reserve
    private lateinit var adapter: AnalyticsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPatientImage = it.getString(Constants.KEY_PATIENT_IMAGE)!!
            mPatientName  = it.getString(Constants.KEY_PATIENT_FIRST_NAME)!! + it.getString(Constants.KEY_PATIENT_LAST_NAME)
            reservation   = it.getParcelable(Constants.Reservation)!!

        }
        mReservationDetailsViewModel = ViewModelProvider(this)[ReservationDetailsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reservationDetailsBinding = FragmentReservationDetailsBinding.inflate(layoutInflater)
        setReservationData()
        setListeners()
        return reservationDetailsBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReservationsDetailsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun setReservationData() {
        reservationDetailsBinding.tvOrderPatientName.text = mPatientName
        reservationDetailsBinding.tvOrderDate.text = reservation.orderDateTime
        reservationDetailsBinding.tvCheckoutSubTotal.text = reservation.orderAnalyticsPrice
        reservationDetailsBinding.tvCheckoutTotalAmount.text = reservation.orderTotalAmount
        reservationDetailsBinding.tvConfirmAdditionalNote.text =
            reservation.orderAdditionalInformation
        reservationDetailsBinding.tvConfirmAddress.text = reservation.orderAddress
        reservationDetailsBinding.tvCheckoutShippingCharge.text = getString(R.string.visit_price)
        when (reservation.orderState) {
            getString(R.string.pending) -> reservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.error_color)
            )
            getString(R.string.in_progress) -> reservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.error)
            )
            getString(R.string.completed) -> reservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.primary_text)
            )
        }
        reservationDetailsBinding.tvOrderState.text = reservation.orderState
        reservationDetailsBinding.ivPatientImage.setImageBitmap(getPatientImage(mPatientImage))
        adapter = AnalyticsAdapter(reservation.analyticsList!!)
        reservationDetailsBinding.rvCartListItems.layoutManager =
            LinearLayoutManager(requireContext())
        reservationDetailsBinding.rvCartListItems.adapter = adapter

    }

    private fun getPatientImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun updateReservation(){
        val reservationMap = HashMap<String,Any>()
        reservationMap[Constants.ORDER_STATE] =getString(R.string.in_progress)

        lifecycleScope.launchWhenResumed {
            mReservationDetailsViewModel.updateReservation(
                reservation.orderId!!,
                reservationMap)
        }


    }
    private fun setListeners(){
        reservationDetailsBinding.btnConfirm.setOnClickListener {
            updateReservation()
            activity?.onBackPressed();

        }
    }

}