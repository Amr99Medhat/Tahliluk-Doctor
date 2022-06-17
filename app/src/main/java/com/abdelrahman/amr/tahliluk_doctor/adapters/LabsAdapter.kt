package com.abdelrahman.amr.tahliluk_doctor.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.tahliluk_doctor.databinding.ItemContainerLabBinding
import com.abdelrahman.amr.tahliluk_doctor.listeners.OnLabClickListener
import com.abdelrahman.amr.tahliluk_doctor.models.Lab

class LabsAdapter: RecyclerView.Adapter<LabsAdapter.LabsViewHolder> {
    private var mLabs: ArrayList<Lab> = ArrayList()
    private lateinit var listener:OnLabClickListener
    constructor(Labs:ArrayList<Lab>,listener:OnLabClickListener){
        mLabs = Labs
        this.listener = listener
    }


    inner class LabsViewHolder(var itemContainerPatientBinding: ItemContainerLabBinding) :
        RecyclerView.ViewHolder(itemContainerPatientBinding.root) {
        @SuppressLint("SetTextI18n")
        fun setLabData(lab: Lab) {
            itemContainerPatientBinding.LabName.text =lab.labName
           itemContainerPatientBinding.LabImageProfile.setImageBitmap(getLabImage(lab.image))
            itemContainerPatientBinding.root.setOnClickListener {
                listener.onLabClickListener(lab)
            }
        }
    }

    private fun getLabImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabsAdapter.LabsViewHolder {
        val binding =
            ItemContainerLabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabsAdapter.LabsViewHolder, position: Int) {
        holder.setLabData(mLabs[position])
    }

    override fun getItemCount(): Int {
        return mLabs.size
    }
}