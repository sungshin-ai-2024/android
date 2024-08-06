package com.example.savewith_android
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemGuardianBinding

class GuardianAdapter(private val guardians: List<ItemGuardian>) : RecyclerView.Adapter<GuardianAdapter.GuardianViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianViewHolder {
        val binding = ItemGuardianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuardianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuardianViewHolder, position: Int) {
        holder.bind(guardians[position])
    }

    override fun getItemCount() = guardians.size

    class GuardianViewHolder(private val binding: ItemGuardianBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(guardian: ItemGuardian) {
            binding.guardian = guardian
            binding.executePendingBindings()
        }
    }
}