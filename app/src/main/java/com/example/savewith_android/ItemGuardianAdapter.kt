package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemGuardianBinding

class ItemGuardianAdapter(private var guardians: List<ItemGuardian>) : RecyclerView.Adapter<ItemGuardianAdapter.GuardianViewHolder>() {

    inner class GuardianViewHolder(private val binding: ItemGuardianBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(guardian: ItemGuardian) {
            binding.guardian = guardian
            binding.executePendingBindings()

            binding.btnDel.setOnClickListener {
                // 해당 보호자 삭제하는 코드
            }
            binding.btnEdit.setOnClickListener {
                // 해당 보호자 정보 수정하는 코드
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianViewHolder {
        val binding = ItemGuardianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuardianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuardianViewHolder, position: Int) {
        holder.bind(guardians[position])
    }

    fun updateItems(newItems: List<ItemGuardian>) {
        guardians = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = guardians.size
}