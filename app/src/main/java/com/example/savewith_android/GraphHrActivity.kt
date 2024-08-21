package com.example.savewith_android

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ActivityGraphHrBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class GraphHrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphHrBinding
    private val viewModel: HrViewModel by viewModels()

    private lateinit var currentDate: LocalDate
    private val chartDataList = mutableListOf<BpChartData>()
    //어댑터 수정 필요
    private lateinit var chart_adapter: BpChartAdapter
    private val bpmDataList = mutableListOf<Int>()

    private var lastBpm: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphHrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentDate = LocalDate.now()
        binding.graphDate.text = currentDate.toString()

        Utils.init(this)

        val adapter = HrActivityAdapter(emptyList())
        binding.graphHrText.layoutManager = LinearLayoutManager(this)
        binding.graphHrText.adapter = adapter

        viewModel.heartRateInfos.observe(this, Observer {
            adapter.updateItems(it)
        })

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)  // 연결 타임아웃을 20초로 설정
            .readTimeout(120, TimeUnit.SECONDS)     // 읽기 타임아웃을 20초로 설정
            .writeTimeout(120, TimeUnit.SECONDS)    // 쓰기 타임아웃을 20초로 설정
            .build()

        val request: Request = Request.Builder()
            .url("ws://210.125.96.132:5000/ws/logger/receive/")
            .build()

        val listener = WebSocketListener { _, _, _, bpmList, _ ->
            runOnUiThread {
                updateChart(bpmList)
                updateText(bpmList)
            }
        }

        client.newWebSocket(request, listener)

        setupRecyclerView()

        binding.graphLeft.setOnClickListener {
            viewModel.loadPreviousDayData()
            currentDate = currentDate.minusDays(1)
            //updateChart()
        }
        binding.graphRight.setOnClickListener {
            viewModel.loadNextDayData()
            currentDate = currentDate.plusDays(1)
            //updateChart()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        // 초기 차트 데이터 로드
        //updateChart()

    }
    private fun setupRecyclerView() {
        chart_adapter = BpChartAdapter(chartDataList)
        binding.graphHr.layoutManager = LinearLayoutManager(this)
        binding.graphHr.adapter = chart_adapter

        chart_adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.graphHr.invalidate() // 차트 새로고침
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun updateChart(bpm: Int) {
        if (lastBpm == bpm) {
            Log.d("GraphHrActivity", "중복된 bpm 데이터: $bpm 추가하지 않음")
            return
        }
        lastBpm = bpm

        bpmDataList.add(bpm)

        // 일정 주기마다 (예: 12초) 그래프를 업데이트
        if (bpmDataList.size % 5 == 0) {  // 예를 들어, 5개의 데이터마다 업데이트
            binding.graphDate.text = currentDate.toString()

            // 저장된 데이터를 기반으로 그래프 생성
            val chartData = createChartDataForDate(currentDate, bpmDataList)
            chartDataList.add(BpChartData(currentDate.toString(), chartData))

            chart_adapter.notifyDataSetChanged()

            // 기존 데이터 초기화 (필요한 경우)
            bpmDataList.clear()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateText(bpm: Int) {
        viewModel.addHrData(bpm)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChartDataForDate(currentDate: LocalDate, bpmDataList: List<Int>): LineData {

        val Infos: List<String> = listOf("심박수")

        val entry1 = ArrayList<Entry>() // 수축기

        val chartData = LineData()

        //SQLite를 사용하는 경우 데이터 불러오기
        /*val db = readableDatabase
        val cursor = db.rawQuery("SELECT val1, val2, val3, val4 FROM your_table WHERE condition", null)

        if (cursor.moveToFirst()) {
            var i = 0
            do {
                val val1 = cursor.getFloat(cursor.getColumnIndexOrThrow("val1"))
                val val2 = cursor.getFloat(cursor.getColumnIndexOrThrow("val2"))

                entry1.add(Entry(i.toFloat(), val1))
                entry2.add(Entry(i.toFloat(), val2))
                i++
            } while (cursor.moveToNext() && i <= 1)
        }
        cursor.close()*/

        // DataSet 추가 및 선 커스텀
        bpmDataList.forEachIndexed { index, bpm ->
            val xValue = index.toFloat()  // X축 값은 리스트의 인덱스
            entry1.add(Entry(xValue, bpm.toFloat()))
        }


        val lineDataSet1 = LineDataSet(entry1, Infos.get(0))
        chartData.addDataSet(lineDataSet1)

        lineDataSet1.lineWidth = 3f
        lineDataSet1.circleRadius = 6f
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawHighlightIndicators(false)
        lineDataSet1.color = Color.rgb(218, 30, 98)
        lineDataSet1.setCircleColor(Color.rgb(218, 30, 98))


        chartData.setValueTextSize(15f)
        return chartData
    }

}