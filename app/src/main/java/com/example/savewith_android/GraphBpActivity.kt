package com.example.savewith_android

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGraphBpBinding
import com.example.savewith_android.databinding.ActivityGraphHrBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate

class GraphBpActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGraphBpBinding
    private val viewModel: BpViewModel by viewModels()
    private lateinit var currentDate: LocalDate
    private val chartDataList = mutableListOf<BpChartData>()
    private lateinit var chart_adapter: BpChartAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = BpAdapter(emptyList())
        binding.graphBpText.layoutManager = LinearLayoutManager(this)
        binding.graphBpText.adapter = adapter

        setupRecyclerView()
        currentDate = LocalDate.now()



        viewModel.bloodPressureInfos.observe(this, Observer {
            adapter.updateItems(it)
        })
        binding.graphLeft.setOnClickListener {
            viewModel.loadPreviousDayData()
            currentDate = currentDate.minusDays(1)
            updateChart()
        }
        binding.graphRight.setOnClickListener {
            viewModel.loadNextDayData()
            currentDate = currentDate.plusDays(1)
            updateChart()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        // 초기 차트 데이터 로드
        updateChart()
    }

    private fun setupRecyclerView() {
        chart_adapter = BpChartAdapter(chartDataList)
        binding.graphBp.layoutManager = LinearLayoutManager(this)
        binding.graphBp.adapter = chart_adapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateChart() {
        binding.graphDate.text = currentDate.toString()

        chartDataList.clear()
        chartDataList.add(BpChartData(currentDate.toString(), createChartDataForDate(currentDate.toString())))

        chart_adapter.notifyDataSetChanged()
    }


    private fun createChartDataForDate(date: String): LineData {

        val Infos: List<String> = listOf("수축기", "이완기")

        val entry1 = ArrayList<Entry>() // 수축기
        val entry2 = ArrayList<Entry>() // 이완기

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

        // 76DataSet 추가 및 선 커스텀

        // 수축기
        val lineDataSet1 = LineDataSet(entry1, Infos.get(0))
        chartData.addDataSet(lineDataSet1)

        lineDataSet1.lineWidth = 3f
        lineDataSet1.circleRadius = 6f
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawHighlightIndicators(false)
        lineDataSet1.color = Color.rgb(103, 206, 103)
        lineDataSet1.setCircleColor(Color.rgb(103, 206, 103))

        // 이완기
        val lineDataSet2 = LineDataSet(entry2, Infos.get(1))
        chartData.addDataSet(lineDataSet2)

        lineDataSet2.lineWidth = 3f
        lineDataSet2.circleRadius = 6f
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setDrawCircleHole(true)
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.setDrawHorizontalHighlightIndicator(false)
        lineDataSet2.setDrawHighlightIndicators(false)
        lineDataSet2.color = Color.rgb(6, 199, 242)
        lineDataSet2.setCircleColor(Color.rgb(6, 199, 242))

        chartData.setValueTextSize(15f)
        return chartData
    }


}