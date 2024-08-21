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
import com.example.savewith_android.databinding.ActivityGraphStressBinding
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

class GraphStressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphStressBinding
    private val viewModel: StressViewModel by viewModels()

    private lateinit var currentDate: LocalDate
    private val chartDataList = mutableListOf<BpChartData>()
    //어댑터 수정 완 (12초)
    private lateinit var chart_adapter: StressChartAdapter
    private lateinit var adapter : StressActivityAdapter
    private lateinit var linedataStress: LineData


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphStressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentDate = LocalDate.now()
        binding.graphDate.text = currentDate.toString()
        Utils.init(this)


        viewModel.stressInfos.observe(this, Observer { stressInfos ->
            adapter.updateItems(stressInfos)
        })

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)  // 연결 타임아웃을 20초로 설정
            .readTimeout(120, TimeUnit.SECONDS)     // 읽기 타임아웃을 20초로 설정
            .writeTimeout(120, TimeUnit.SECONDS)    // 쓰기 타임아웃을 20초로 설정
            .build()

        val request: Request = Request.Builder()
            .url("ws://210.125.96.132:5000/ws/logger/receive/")
            .build()

        val listener = WebSocketListener { ppgDataList, ppgPrediction, accPredictList, _, _ ->
            runOnUiThread {
                updateChart(ppgDataList)
                updateText(ppgPrediction)
            }
        }

        client.newWebSocket(request, listener)
        //client.dispatcher.executorService.shutdown()



        setupRecyclerView()


        binding.graphLeft.setOnClickListener {
            viewModel.loadPreviousDayData()
            currentDate = currentDate.minusDays(1)
            //updateChart(ppgDataList)
        }

        binding.graphRight.setOnClickListener {
            viewModel.loadNextDayData()
            currentDate = currentDate.plusDays(1)
            //updateChart(ppgDataList)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        // 초기 차트 데이터 로드
        //updateChart(ppgDataList)

        if (chartDataList.isEmpty()) {
            Log.e("ChartError", "No data available to display")
        }

    }

    private fun setupRecyclerView() {
        chart_adapter = StressChartAdapter(chartDataList)
        binding.graphStress.layoutManager = LinearLayoutManager(this)
        binding.graphStress.adapter = chart_adapter

        adapter = StressActivityAdapter(emptyList())
        binding.graphStressText.layoutManager = LinearLayoutManager(this)
        binding.graphStressText.adapter = adapter

        chart_adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.graphStress.invalidate() // 차트 새로고침
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun updateChart(ppgDataList: List<Float>) {
        Log.d("ChartUpdate", "Updating chart with data: $ppgDataList")

        val chartData = createChartDataForDate(currentDate, ppgDataList)
        if (chartData.dataSets.isNotEmpty()) {
            chartDataList.add(BpChartData(currentDate.toString(), chartData))
        } else {
            Log.e("ChartError", "No data available in chartData")
        }

        chart_adapter.notifyDataSetChanged()

        Log.d("Debug", "updateChart finished")
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateText(ppgPrediction: Any) {
        Log.d("ChartUpdate", "Updating chart with data: $ppgPrediction")

        val predictions = WebSocketDataManager.predictions
        for (prediction in predictions!!){
            viewModel.addStressData(prediction as Float)
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChartDataForDate(currentDate: LocalDate, ppgDataList: List<Float>): LineData {

        Log.d("DataCheck", "ppgDataString: $ppgDataList")

        val entry1 = ArrayList<Entry>()
        if (ppgDataList.isNotEmpty()) {
            var currentDateTime = currentDate.atStartOfDay()

            for (i in ppgDataList.indices) {
                val xValue = i.toFloat()
                val yValue = ppgDataList[i]

                // 중복된 X 값이 있는지 확인하고 추가
                if (entry1.none { it.x == xValue && it.y == yValue }) {
                    entry1.add(Entry(xValue, yValue))
                    Log.d("TimeMapping", "Entry $i corresponds to ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))}")
                    currentDateTime = currentDateTime.plusSeconds(1)
                }
            }
        } else {
            Log.e("ChartError", "ppgDataList is empty, no entries created")
        }

        if (entry1.isEmpty()) {
            Log.e("ChartError", "No entries in the LineDataSet")
        }

        val lineDataSet1 = LineDataSet(entry1, "스트레스")
        lineDataSet1.lineWidth = 1f
        lineDataSet1.circleRadius = 1f
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawHighlightIndicators(false)
        lineDataSet1.color = Color.rgb(30, 111, 218)
        lineDataSet1.setCircleColor(Color.rgb(30, 111, 218))


        // LineData에 DataSet 추가
        val chartData = LineData()
        chartData.addDataSet(lineDataSet1)
        if (chartData.dataSets.isEmpty()) {
            Log.e("ChartError", "No data available in chartData")
        }


        // 차트 데이터 반환
        chartData.setValueTextSize(15f)
        return chartData
        // JSONArray로 변환한 후 처리


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

    }

}