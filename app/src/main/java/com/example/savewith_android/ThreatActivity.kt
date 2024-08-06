package com.example.savewith_android

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityThreatBinding

class ThreatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityThreatBinding
    private val viewModel_count: ThreatCountViewModel by viewModels()
    private val viewModel_log: ThreatLogViewModel by viewModels()
    private val viewModel_contact: ThreatContactViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter_count = ThreatCountAdapter(emptyList())
        val adapter_log = ThreatLogAdapter(emptyList())
        val adapter_contact = ThreatContactAdapter(this, emptyList())

        binding.threatAlert.layoutManager = LinearLayoutManager(this)
        binding.threatAlert.adapter = adapter_count

        binding.alertHistoryText.layoutManager = LinearLayoutManager(this)
        binding.alertHistoryText.adapter = adapter_log

        binding.contactDetails.layoutManager = LinearLayoutManager(this)
        binding.contactDetails.adapter = adapter_contact

        viewModel_count.incidentInfos.observe(this, Observer {
            adapter_count.updateItems(it)
        })

        viewModel_log.threatLogInfos.observe(this, Observer {
            adapter_log.updateItems(it)
        })

        viewModel_contact.contacts.observe(this, Observer { contacts ->
            adapter_contact.updateItems(contacts)
        })

        binding.threatLeft.setOnClickListener {
            viewModel_count.loadPreviousDayData()
        }

        binding.threatRight.setOnClickListener {
            viewModel_count.loadNextDayData()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnAddGuardian.setOnClickListener {
            // 보호자 추가 액티비티 실행
        }
    }
}