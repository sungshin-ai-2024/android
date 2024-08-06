package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemDescribeBpBinding

class BpAdapter(private var items: List<BloodPressureInfo>) : RecyclerView.Adapter<BpAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDescribeBpBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: BloodPressureInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDescribeBpBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<BloodPressureInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}