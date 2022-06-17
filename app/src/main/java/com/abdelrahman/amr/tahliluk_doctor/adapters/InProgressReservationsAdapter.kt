package com.abdelrahman.amr.tahliluk_doctor.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.databinding.ItemContainerInprogressReservationsBinding
import com.abdelrahman.amr.tahliluk_doctor.databinding.ItemContainerReservationsBinding
import com.abdelrahman.amr.tahliluk_doctor.listeners.ReservationListener
import com.abdelrahman.amr.tahliluk_doctor.models.Reserve




class InProgressReservationsAdapter : RecyclerView.Adapter<InProgressReservationsAdapter.ReservationsViewHolder> {
    private  lateinit var reservationsList:ArrayList<Reserve>
    constructor()
    constructor(reservationsList: ArrayList<Reserve> ){
        this.reservationsList = reservationsList
    }


   inner class ReservationsViewHolder(private var binding : ItemContainerInprogressReservationsBinding)
       :RecyclerView.ViewHolder(
       binding.root) {
       fun setReservationData(reserve: Reserve) {
           binding.tvOrderprice.text = reserve.orderTotalAmount
           binding.tvOrderState.text = reserve.orderState
           binding.tvOrderAddress.text = reserve.orderAddress
           binding.tvOrderDate.text = reserve.orderDateTime

           when(reserve.orderState){
               "Pending"  -> binding.tvOrderState.setTextColor(Color. parseColor("#B00020"))
               "Accepted" -> binding.tvOrderState.setTextColor(Color. parseColor("#284693"))
               "Completed" -> binding.tvOrderState.setTextColor(Color. parseColor("#284693"))
           }


       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        val binding =
            ItemContainerInprogressReservationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) {
       holder.setReservationData(reservationsList[position])

    }

    override fun getItemCount(): Int {
        return reservationsList.size
    }

}