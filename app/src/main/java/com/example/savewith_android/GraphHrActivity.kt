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
import com.example.savewith_android.databinding.ActivityGraphHrBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate

class GraphHrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphHrBinding
    private val viewModel: HrViewModel by viewModels()

    private lateinit var currentDate: LocalDate
    private val chartDataList = mutableListOf<BpChartData>()
    private lateinit var chart_adapter: BpChartAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphHrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = HrActivityAdapter(emptyList())
        binding.graphHrText.layoutManager = LinearLayoutManager(this)
        binding.graphHrText.adapter = adapter

        viewModel.heartRateInfos.observe(this, Observer {
            adapter.updateItems(it)
        })

        setupRecyclerView()
        currentDate = LocalDate.now()

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
        binding.graphHr.layoutManager = LinearLayoutManager(this)
        binding.graphHr.adapter = chart_adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateChart() {
        binding.graphDate.text = currentDate.toString()

        chartDataList.clear()
        chartDataList.add(BpChartData(currentDate.toString(), createChartDataForDate(currentDate.toString())))

        chart_adapter.notifyDataSetChanged()
    }


    private fun createChartDataForDate(date: String): LineData {

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
        lineDataSet1.color = Color.rgb(218, 30, 98)
        lineDataSet1.setCircleColor(Color.rgb(218, 30, 98))


        chartData.setValueTextSize(15f)
        return chartData
    }

}