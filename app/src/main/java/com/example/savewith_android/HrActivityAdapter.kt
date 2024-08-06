package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemDescribeBinding


class HrActivityAdapter (private var items: List<HeartRateInfo>) : RecyclerView.Adapter<HrActivityAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDescribeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: HeartRateInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDescribeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<HeartRateInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}