package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemDeviceBinding

class ItemDeviceAdapter(private val devices: List<ItemDevice>) : RecyclerView.Adapter<ItemDeviceAdapter.ItemDeviceViewHolder>() {
    class ItemDeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: ItemDevice) {
            binding.device = device
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDeviceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDeviceBinding.inflate(layoutInflater, parent, false)
        return ItemDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount() = devices.size
}