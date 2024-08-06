package com.example.savewith_android

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGraphHrBinding
import com.example.savewith_android.databinding.ActivityGraphStressBinding

class GraphStressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphStressBinding
    private val viewModel: StressViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphStressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = StressActivityAdapter(emptyList())
        binding.graphStressText.layoutManager = LinearLayoutManager(this)
        binding.graphStressText.adapter = adapter

        viewModel.stressInfos.observe(this, Observer {
            adapter.updateItems(it)
        })

        binding.graphLeft.setOnClickListener {
            viewModel.loadPreviousDayData()
        }

        binding.graphRight.setOnClickListener {
            viewModel.loadNextDayData()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }


    }

}