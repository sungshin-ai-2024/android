package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemStateDayBinding

class DayAdapter (private var items: List<EventLogInfo>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStateDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: EventLogInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStateDayBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<EventLogInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}