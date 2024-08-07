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

            // ItemGuard.xml에서 클릭할 수 있는 게 있으면
            // 해당 기능 실행하는 코드 추가하기
            // 예를 들어, 보호자 정보 수정하기 등의 버튼을 만들었으면 그것을 클릭했을 경우
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