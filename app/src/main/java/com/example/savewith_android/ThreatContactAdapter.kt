package com.example.savewith_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemThreatContactorBinding

class ThreatContactAdapter(private val context: Context, private var items: List<Contact>) : RecyclerView.Adapter<ThreatContactAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemThreatContactorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.tvName.text = contact.name
            binding.tvNumber.text = contact.phoneNumber

            binding.tvThreatCall.setOnClickListener {
                // 전화 걸기 인텐트 실행
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${contact.phoneNumber}")
                context.startActivity(intent)
            }

            binding.tvThreatText.setOnClickListener {
                // 문자 보내기 인텐트 실행
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("smsto:${contact.phoneNumber}")
                intent.putExtra("sms_body", "문자 내용을 입력하세요")
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemThreatContactorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Contact>) {
        items = newItems
        notifyDataSetChanged()
    }
}