package com.example.savewith_android

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityMapBinding
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

class MapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMapBinding
    private val maplist = ArrayList<LocationData>()
    private val viewModel: MapViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MapActivityRecyclerViewAdapter(emptyList())
        binding.mapLocationText.layoutManager = LinearLayoutManager(this)
        binding.mapLocationText.adapter = adapter

        viewModel.locationTimeRanges.observe(this, Observer {
            adapter.updateItems(it)
        })

    }
}