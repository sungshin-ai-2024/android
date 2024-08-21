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
import com.example.savewith_android.databinding.ActivityGraphDayBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Suppress("UNUSED_EXPRESSION")
class GraphDayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphDayBinding
    private val viewModel: DayViewModel by viewModels()

    private lateinit var currentDate: LocalDate
    private val chartDataList = mutableListOf<BpChartData>()
    private lateinit var chart_adapter: BpChartAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphDayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentDate = LocalDate.now()

        binding.graphDate.text = currentDate.toString()

        Utils.init(this)

        val adapter = DayAdapter(emptyList())
        binding.graphText.layoutManager = LinearLayoutManager(this)
        binding.graphText.adapter = adapter

        setupRecyclerView()


        viewModel.eventLogInfos.observe(this, Observer {
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

        val listener = WebSocketListener { _, predictionList, accPredictionList, _, _ ->
            runOnUiThread {
                updateChart(predictionList, accPredictionList)
                updateText(predictionList, accPredictionList)
            }
        }

        client.newWebSocket(request, listener)

        setupRecyclerView()
        currentDate = LocalDate.now()

        binding.graphLeft.setOnClickListener {
            viewModel.loadPreviousDayData()
            currentDate = currentDate.minusDays(1)
            //updateChart(ppgDataList)
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
        binding.graphAllDay.layoutManager = LinearLayoutManager(this)
        binding.graphAllDay.adapter = chart_adapter

        chart_adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.graphAllDay.invalidate() // 차트 새로고침
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun updateChart(predictionList: List<Float>, accPredictionList: List<Float>) {

        val chartData = createChartDataForDate(currentDate, predictionList, accPredictionList)
        if (chartData.dataSets.isNotEmpty()) {
            chartDataList.add(BpChartData(currentDate.toString(), chartData))
        } else {
            Log.e("ChartError", "No data available in chartData")
        }

        chart_adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun updateText(predictionList: List<Float>, accPredictionList: List<Float>) {

        val highThreshold = 0.74
        val highCount = predictionList.count {
            val predictionValue = it as? Float ?: return@count false
            predictionValue >= highThreshold
        }
        val highPercentage = (highCount.toDouble() / predictionList.size) * 100

        val prediction = when {
            highPercentage >= 70 -> 1F
            highPercentage >= 30 -> 0F
            else -> 0F
        }

        // accPredictions에서 첫 번째 값을 사용 (두 번째 값이 맞는지 확인 필요)
        val fallDetected = accPredictionList.getOrNull(1) ?: 0F

        // 이제 prediction과 fallDetected를 한 번만 viewModel에 추가
        viewModel.addDayData(prediction, fallDetected)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChartDataForDate(
        currentDate: LocalDate, predictionList: List<Float>,
        accPredictionList: List<Float>
    ): LineData {


        val Infos: List<String> = listOf("스트레스", "낙상")

        val entry1 = ArrayList<Entry>() // 스트레스
        val entry2 = ArrayList<Entry>() // 낙상

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
        if (predictionList.isNotEmpty() && accPredictionList.isNotEmpty()) {
            var currentDateTime = currentDate.atStartOfDay()

            val predictions = WebSocketDataManager.predictions

            if (predictions != null) {
                for (i in predictions.indices) {
                    val highThreshold = 0.74
                    val highCount = predictions.count {
                        val predictionValue = it as? Double ?: return@count false
                        predictionValue >= highThreshold
                    }
                    val highPercentage = (highCount.toDouble() / predictionList.size) * 100

                    val prediction = when {
                        highPercentage >= 70 -> 1F
                        highPercentage >= 30 -> 0F
                        else -> 0F
                    }

                    entry1.add(Entry(i.toFloat(), prediction))
                    Log.d("TimeMapping", "Entry $i corresponds to ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))}")
                    currentDateTime = currentDateTime.plusSeconds(12)
                }
            }



            for (i in accPredictionList.indices) {
                val fallDetected = if (accPredictionList[1] == 1f) 1f else 0f
                entry2.add(Entry(i.toFloat(), fallDetected))
                Log.d("TimeMapping", "Entry $i corresponds to ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))}")
                currentDateTime = currentDateTime.plusSeconds(12)
            }

        } else {
            Log.e("ChartError", "ppgDataList is empty, no entries created")
        }

        if (entry1.isEmpty() || entry2.isEmpty()) {
            Log.e("ChartError", "No entries in the LineDataSet")
        }

        // 스트레스
        val lineDataSet1 = LineDataSet(entry1, Infos[0])
        chartData.addDataSet(lineDataSet1)

        lineDataSet1.lineWidth = 3f
        lineDataSet1.circleRadius = 6f
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawHighlightIndicators(false)
        lineDataSet1.color = Color.rgb(30, 111, 218)
        lineDataSet1.setCircleColor(Color.rgb(30, 111, 218))

        // 심박수
        val lineDataSet2 = LineDataSet(entry2, Infos[1])
        chartData.addDataSet(lineDataSet2)

        lineDataSet2.lineWidth = 3f
        lineDataSet2.circleRadius = 6f
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setDrawCircleHole(true)
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.setDrawHorizontalHighlightIndicator(false)
        lineDataSet2.setDrawHighlightIndicators(false)
        lineDataSet2.color = Color.rgb(103, 206, 103)
        lineDataSet2.setCircleColor(Color.rgb(103, 206, 103))

        chartData.setValueTextSize(15f)
        return chartData
    }
}