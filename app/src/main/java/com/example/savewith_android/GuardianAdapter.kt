package com.example.savewith_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemGuardianBinding

class GuardianAdapter(private val listener: GuardianActionListener) :
    RecyclerView.Adapter<GuardianAdapter.GuardianViewHolder>() {

    private var guardians: List<Guardian> = listOf()

    inner class GuardianViewHolder(private val binding: ItemGuardianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guardian: Guardian, position: Int) {
            binding.textGuardianName.text = guardian.name
            binding.textGuardianRelationship.text = guardian.relationship
            binding.textGuardianContact.text = guardian.phone_number

            binding.btnEdit.setOnClickListener {
                listener.onEditGuardian(guardian.copy(id = position + 1))
            }

            binding.btnDel.setOnClickListener {
                listener.onDeleteGuardian(guardian.copy(id = position + 1))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianViewHolder {
        val binding = ItemGuardianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuardianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuardianViewHolder, position: Int) {
        holder.bind(guardians[position], position)
    }

    override fun getItemCount(): Int = guardians.size

    fun updateGuardians(newGuardians: List<Guardian>) {
        guardians = newGuardians
        notifyDataSetChanged()
    }
}