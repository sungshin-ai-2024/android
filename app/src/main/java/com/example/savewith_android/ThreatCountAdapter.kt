package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemThreatBinding

class ThreatCountAdapter(private var items: List<ThreatCountInfo>) : RecyclerView.Adapter<ThreatCountAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemThreatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: ThreatCountInfo) {
            binding.info = info
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemThreatBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<ThreatCountInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}