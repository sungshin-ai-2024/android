package com.example.savewith_android

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemGuardianBinding

class GuardianAdapter(private val listener: GuardianActionListener) :
    RecyclerView.Adapter<GuardianAdapter.GuardianViewHolder>() {

    private var guardians: List<Guardian> = listOf()

    inner class GuardianViewHolder(private val binding: ItemGuardianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guardian: Guardian) {
            binding.textGuardianName.text = guardian.name
            binding.textGuardianRelationship.text = guardian.relationship
            binding.textGuardianContact.text = guardian.phone_number

            binding.btnEdit.setOnClickListener {
                Log.d("GuardianAdapter", "Edit button clicked for guardian: ${guardian.name}")
                listener.onEditGuardian(guardian)
            }

            binding.btnDel.setOnClickListener {
                Log.d("GuardianAdapter", "Delete button clicked for guardian: ${guardian.name}")
                listener.onDeleteGuardian(guardian)
            }

            binding.btnEdit.isClickable = true
            binding.btnDel.isClickable = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianViewHolder {
        val binding = ItemGuardianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuardianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuardianViewHolder, position: Int) {
        holder.bind(guardians[position])
    }

    override fun getItemCount(): Int = guardians.size

    fun updateGuardians(newGuardians: List<Guardian>) {
        guardians = newGuardians
        notifyDataSetChanged()
    }
}