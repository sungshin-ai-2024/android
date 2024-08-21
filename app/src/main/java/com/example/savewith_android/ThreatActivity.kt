package com.example.savewith_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityThreatBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class ThreatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityThreatBinding
    private val viewmodelCount: ThreatCountViewModel by viewModels()
    private val viewmodelLog: ThreatLogViewModel by viewModels()
    private val viewmodelContact: ThreatContactViewModel by viewModels()
    private lateinit var currentDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentDate = LocalDate.now()
        binding.threatDate.text = currentDate.toString()

        val adapterCount = ThreatCountAdapter(emptyList())
        val adapterLog = ThreatLogAdapter(emptyList())
        val adapterContact = ThreatContactAdapter(this, emptyList())

        binding.threatAlert.layoutManager = LinearLayoutManager(this)
        binding.threatAlert.adapter = adapterCount

        binding.alertHistoryText.layoutManager = LinearLayoutManager(this)
        binding.alertHistoryText.adapter = adapterLog

        binding.contactDetails.layoutManager = LinearLayoutManager(this)
        binding.contactDetails.adapter = adapterContact

        viewmodelCount.incidentInfos.observe(this, Observer {
            adapterCount.updateItems(it)
        })

        viewmodelLog.threatLogInfos.observe(this, Observer {
            adapterLog.updateItems(it)
        })

        viewmodelContact.getGuardians().observe(this, Observer { guardianList ->
            adapterContact.updateItems(guardianList)
        })

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)  // 연결 타임아웃을 20초로 설정
            .readTimeout(120, TimeUnit.SECONDS)     // 읽기 타임아웃을 20초로 설정
            .writeTimeout(120, TimeUnit.SECONDS)    // 쓰기 타임아웃을 20초로 설정
            .build()

        val request: Request = Request.Builder()
            .url("ws://210.125.96.132:5000/ws/logger/receive/")
            .build()

        val listener = WebSocketListener { _, ppgPrediction, accPredictionList, _, _ ->
            runOnUiThread {
                updateCount(ppgPrediction, accPredictionList)
                updateLog(ppgPrediction, accPredictionList)
            }
        }

        client.newWebSocket(request, listener)

        binding.threatLeft.setOnClickListener {
            viewmodelCount.loadPreviousDayData()
            currentDate = currentDate.minusDays(1)
        }

        binding.threatRight.setOnClickListener {
            viewmodelCount.loadNextDayData()
            currentDate = currentDate.plusDays(1)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddGuardian.setOnClickListener {
            // 보호자 추가 액티비티 실행
            val intent = Intent(this, AddGuardActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCount(ppgPrediction: List<Float>, accPredictionList: List<Float>){
        viewmodelCount.addCountData(ppgPrediction, accPredictionList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLog(ppgPrediction: List<Float>, accPredictionList: List<Float>){
        viewmodelLog.addThreatLog(ppgPrediction, accPredictionList)
    }
}