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
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.adapters.AnalyticsAdapter
import com.abdelrahman.amr.tahliluk_doctor.databinding.FragmentInProgressReservationDetailsBinding
import com.abdelrahman.amr.tahliluk_doctor.databinding.FragmentInProgressReservationsBinding
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants

class InProgressReservationDetailsFragment : Fragment() {
    private lateinit var mInProgressReservationDetailsFragment: FragmentInProgressReservationDetailsBinding
    private lateinit var mPatientName :String
    private lateinit var mPatientImage :String

    private lateinit var reservation: Reserve
    private lateinit var adapter: AnalyticsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPatientImage = it.getString(Constants.KEY_PATIENT_IMAGE)!!
            mPatientName  = it.getString(Constants.KEY_PATIENT_FIRST_NAME)!! + it.getString(
                Constants.KEY_PATIENT_LAST_NAME)
            reservation   = it.getParcelable(Constants.Reservation)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mInProgressReservationDetailsFragment = FragmentInProgressReservationDetailsBinding.inflate(layoutInflater)
       setReservationData()
        return mInProgressReservationDetailsFragment.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InProgressReservationDetailsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun setReservationData() {
        mInProgressReservationDetailsFragment.tvOrderPatientName.text = mPatientName
        mInProgressReservationDetailsFragment.tvOrderDate.text = reservation.orderDateTime
        mInProgressReservationDetailsFragment.tvCheckoutSubTotal.text = reservation.orderAnalyticsPrice
        mInProgressReservationDetailsFragment.tvCheckoutTotalAmount.text = reservation.orderTotalAmount
        mInProgressReservationDetailsFragment.tvConfirmAdditionalNote.text =
            reservation.orderAdditionalInformation
        mInProgressReservationDetailsFragment.tvConfirmAddress.text = reservation.orderAddress
        mInProgressReservationDetailsFragment.tvCheckoutShippingCharge.text = getString(R.string.visit_price)
        when (reservation.orderState) {
            getString(R.string.pending) -> mInProgressReservationDetailsFragment.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.error_color)
            )
            getString(R.string.in_progress) -> mInProgressReservationDetailsFragment.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.error)
            )
            getString(R.string.completed) -> mInProgressReservationDetailsFragment.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.primary_text)
            )
        }
        mInProgressReservationDetailsFragment.tvOrderState.text = reservation.orderState
        mInProgressReservationDetailsFragment.ivPatientImage.setImageBitmap(getPatientImage(mPatientImage))
        adapter = AnalyticsAdapter(reservation.analyticsList!!)
        mInProgressReservationDetailsFragment.rvCartListItems.layoutManager =
            LinearLayoutManager(requireContext())
        mInProgressReservationDetailsFragment.rvCartListItems.adapter = adapter

    }

    private fun getPatientImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}