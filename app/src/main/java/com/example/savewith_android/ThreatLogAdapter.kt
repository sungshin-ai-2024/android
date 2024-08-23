package com.example.savewith_android

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemDescribeThreatBinding

class ThreatLogAdapter(private var items: List<ThreatLogInfo>) : RecyclerView.Adapter<ThreatLogAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDescribeThreatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: ThreatLogInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDescribeThreatBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<ThreatLogInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}