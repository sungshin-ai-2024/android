package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemDescribeStressBinding

class StressActivityAdapter(private var items: List<StressInfo>) : RecyclerView.Adapter<StressActivityAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDescribeStressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: StressInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDescribeStressBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<StressInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}