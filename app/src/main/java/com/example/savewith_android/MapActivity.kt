package com.example.savewith_android

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityMapBinding
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class MapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMapBinding
    private val maplist = ArrayList<LocationData>()
    private val viewModel: MapViewModel by viewModels()

    private lateinit var currentDate: LocalDate
    private lateinit var locationAdapter: MapActivityLocationAdapter
    private val locationList = mutableListOf<Location>()

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

        locationList.add(Location("").apply { latitude = 37.7749; longitude = -122.4194 }) // 샌프란시스코
        locationList.add(Location("").apply { latitude = 34.0522; longitude = -118.2437 }) // 로스앤젤레스

        binding.mapLocation.layoutManager = LinearLayoutManager(this)
        locationAdapter = MapActivityLocationAdapter(this, locationList)
        binding.mapLocation.adapter = locationAdapter

        currentDate = LocalDate.now()
        binding.mapLeft.setOnClickListener {
            currentDate = currentDate.minusDays(1)
            fetchDataForDate(currentDate)
        }
        binding.mapRight.setOnClickListener {
            currentDate = currentDate.plusDays(1)
            fetchDataForDate(currentDate)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        fetchCurrentData()
    }

    private fun fetchCurrentData() {
        // 현재 날짜로 데이터를 가져옵니다.
        fetchDataForDate(currentDate)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDataForDate(date: LocalDate) {
        // 주어진 날짜에 해당하는 정보를 가져오는 로직 구현
        // 예를 들어, API 호출 또는 데이터베이스 조회 등

        // 여기서는 예시로 주어진 날짜를 이용하여 위치 데이터를 추가
        locationList.clear()
        // 예시로 고정된 위치를 추가 (실제 날짜에 해당하는 데이터를 가져오도록 수정)
        locationList.add(Location("").apply { latitude = 40.7128; longitude = -74.0060 }) // 뉴욕
        locationList.add(Location("").apply { latitude = 51.5074; longitude = -0.1278 }) // 런던

        // RecyclerView 업데이트
        locationAdapter.notifyDataSetChanged()
    }

}