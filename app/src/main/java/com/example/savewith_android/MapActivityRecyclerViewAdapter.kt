package com.example.savewith_android

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemLocationBinding

class MapActivityRecyclerViewAdapter(private var items: List<LocationTimeRange>) : RecyclerView.Adapter<MapActivityRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationTimeRange) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<LocationTimeRange>) {
        items = newItems
        notifyDataSetChanged()
    }

}