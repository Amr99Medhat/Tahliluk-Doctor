
package com.abdelrahman.amr.tahliluk_doctor.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.tahliluk_doctor.databinding.ItemContainerAnalysisBinding
import com.abdelrahman.amr.tahliluk_doctor.models.Analytics

class AnalyticsAdapter(private var analytics: ArrayList<Analytics>) :
    RecyclerView.Adapter<AnalyticsAdapter.AnalyticsViewHolder>() {

    inner class AnalyticsViewHolder(var binding: ItemContainerAnalysisBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setAnalyticData(analyses: Analytics) {
            binding.textAnalysisName.text = analyses.analysis_name
            binding.textAnalysisPrice.text = analyses.analysis_price

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsViewHolder {
        val binding =
            ItemContainerAnalysisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalyticsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalyticsViewHolder, position: Int) {
        holder.setAnalyticData(analytics[position])
    }

    override fun getItemCount(): Int {
        return  analytics.size
    }

}
