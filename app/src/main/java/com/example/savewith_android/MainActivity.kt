package com.example.savewith_android

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import at.grabner.circleprogress.CircleProgressView
import com.example.savewith_android.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private lateinit var runnable: Runnable
    private lateinit var currentDate: LocalDate

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lineChart: LineChart = binding.mainGraphAll
        configureChartAppearance(lineChart)

        currentDate = LocalDate.now()

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)  // 연결 타임아웃을 20초로 설정
            .readTimeout(120, TimeUnit.SECONDS)     // 읽기 타임아웃을 20초로 설정
            .writeTimeout(120, TimeUnit.SECONDS)    // 쓰기 타임아웃을 20초로 설정
            .build()

        val request: Request = Request.Builder()
            .url("ws://210.125.96.132:5000/ws/logger/receive/")
            .build()

        val listener = WebSocketListener { _, ppgPrediction, accPredictionList, bpm, step ->
            runOnUiThread {
                binding.tvStressCount.text = updateText(ppgPrediction)
                prepareChartData(createChartData(currentDate, ppgPrediction, accPredictionList), lineChart)
                binding.tvHrCount.text = bpm.toString()
                binding.tvBpCount.text = step.toString()

                val alertValue = updateAlert(ppgPrediction, accPredictionList)

                when (alertValue) {
                    1 -> {
                        // 기존의 tvAlertCount 값 가져오기
                        val currentValue = binding.tvFallCount.text.toString().toIntOrNull() ?: 0
                        // 값이 1이면 1을 더함
                        binding.tvFallCount.text = (currentValue + 1).toString()
                    }
                    2 -> {
                        val currentValue = binding.tvThreatSituationCount.text.toString().toIntOrNull() ?: 0
                        binding.tvThreatSituationCount.text = (currentValue + 1).toString()
                    }
                    else -> {
                        val currentValue1 = binding.tvFallCount.text.toString().toIntOrNull() ?: 0
                        val currentValue2 = binding.tvThreatSituationCount.text.toString().toIntOrNull() ?: 0
                        binding.tvFallCount.text = (currentValue1 + 0).toString()
                        binding.tvThreatSituationCount.text = (currentValue2 + 0).toString()
                    }
                }
            }
        }

        client.newWebSocket(request, listener)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_map, MapFragment())
            .commit()

        runnable = Runnable {
            updateTime()
            handler.postDelayed(runnable, 60000) // 1분마다 실행
        }
        handler.post(runnable) // 초기 실행


        binding.btnGraph.setOnClickListener {
            // graph 페이지로 이동
            val intent = Intent(this, GraphDayActivity::class.java)
            startActivity(intent)
        }

        binding.mainBoxHr.setOnClickListener {
            // 심박수 페이지로 이동
            val intent = Intent(this, GraphHrActivity::class.java)
            startActivity(intent)
        }

        binding.mainBoxStress.setOnClickListener {
            //스트레스 페이지로 이동
            val intent = Intent(this, GraphStressActivity::class.java)
            startActivity(intent)
        }

        binding.mainBoxBp.setOnClickListener {
            // 혈압 페이지로 이동
            val intent = Intent(this, GraphBpActivity::class.java)
            startActivity(intent)
        }

        binding.btnThreat.setOnClickListener {
            // 위험 감지 페이지로 이동
            val intent = Intent(this, ThreatActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetting.setOnClickListener {
            // 설정 페이지로 이동
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        // 시간 업데이트
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    binding.tvThreatDate.text = dateFormat.format(Date())
                }
            }
        }

        val timer = Timer()
        timer.schedule(timerTask, 0, 1000)

    }
    private fun updateTime() {
        val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())
        val currentdate = dateFormat.format(Date())
        val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
        val currentTime = timeFormat.format(Date())
        binding.tvMainDate.text = currentdate
        binding.tvMainTime.text = currentTime
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // 액티비티가 종료될 때 핸들러 콜백 제거
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 권한이 승인되면 MapFragment의 위치를 설정
            val mapFragment = supportFragmentManager.findFragmentById(R.id.main_map) as? MapFragment
            mapFragment?.enableMyLocation()
        }
    }

    private fun updateText(ppgPrediction: Any): String {
        Log.d("ChartUpdate", "Updating chart with data: $ppgPrediction")

        val predictions = WebSocketDataManager.predictions

        return if (!predictions.isNullOrEmpty()) {
            val highThreshold = 0.74
            val highCount = predictions.count {
                val predictionValue = it as? Double ?: return@count false
                predictionValue >= highThreshold
            }
            val highPercentage = (highCount.toDouble() / predictions.size) * 100

            when {
                highPercentage >= 70 -> "높음"
                highPercentage >= 30 -> "보통"
                else -> "낮음"
            }
        } else {
            "데이터 없음" // predictions가 null이거나 비어있는 경우 반환할 기본값
        }
    }

    private fun updateAlert(ppgPrediction: Any, accPredictionList: List<Float>) : Int {
        val prediction = updateText(ppgPrediction)
        val accPrediction = accPredictionList[1]

        // 기본 반환값을 0으로 설정
        var alert = 0

        // accPrediction이 2인 경우 1 반환
        if (accPrediction == 2f) {
            alert = 1
        }

        // prediction이 "높음"이면서 accPrediction이 2인 경우 2 반환
        if (prediction == "높음" && accPrediction == 2f) {
            alert = 2
        }

        return alert

    }


    private fun configureChartAppearance(lineChart: LineChart) {
        lineChart.extraBottomOffset = 15f // 간격
        lineChart.description.isEnabled = false // chart 밑에 description 표시 유무

        // Legend는 차트의 범례
        val legend: Legend = lineChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 13f
        legend.textSize = 13f
        legend.textColor = Color.parseColor("#242424")
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.yEntrySpace = 5f
        legend.isWordWrapEnabled = true
        legend.xOffset = 80f
        legend.yOffset = 20f

        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        val xAxis = lineChart.xAxis
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM // x축 데이터 표시 위치
        xAxis.granularity = 1f
        xAxis.textSize = 14f
        xAxis.textColor = Color.rgb(118, 118, 118)
        xAxis.spaceMin = 0.5f // Chart 맨 왼쪽 간격 띄우기
        xAxis.spaceMax = 0.5f // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.textSize = 14f
        yAxisLeft.textColor = Color.rgb(163, 163, 163)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.axisLineWidth = 2f
        yAxisLeft.axisMinimum = 0f // 최솟값
        yAxisLeft.axisMaximum = 1f // 최댓값
        yAxisLeft.granularity = 1f

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        val yAxis = lineChart.axisRight
        yAxis.setDrawLabels(false) // label 삭제
        yAxis.textColor = Color.rgb(163, 163, 163)
        yAxis.setDrawAxisLine(false)
        yAxis.axisLineWidth = 2f
        yAxis.axisMinimum = 0f // 최솟값
        yAxis.axisMaximum = 1f // 최댓값
        yAxis.granularity = 1f

        // XAxis에 원하는 String 설정하기 (날짜)
        xAxis.valueFormatter = object : ValueFormatter() {
            @SuppressLint("DefaultLocale")
            override fun getFormattedValue(value: Float): String {
                val currentTimeMillis = System.currentTimeMillis()
                val displayedTime = currentTimeMillis + (value * 12000).toLong() // 12초 단위로 증가
                return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(displayedTime))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChartData(currentDate: LocalDate, predictionList: List<Float>,
                                accPredictionList: List<Float>): LineData {

        val Infos: List<String> = listOf("스트레스", "낙상")

        val entry1 = ArrayList<Entry>() // 앱1
        val entry2 = ArrayList<Entry>() // 앱2

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

        // 4개 앱의 DataSet 추가 및 선 커스텀

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
                    Log.d("TimeMapping", "Entry $i corresponds to ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentDateTime.atZone(
                        ZoneId.systemDefault()).toInstant().toEpochMilli()))}")
                    currentDateTime = currentDateTime.plusSeconds(12)
                }
            }



            for (i in accPredictionList.indices) {
                val accPrediction = accPredictionList[1]
                val fallDetected = if (accPrediction == 2f) 1f else 0f
                //val offsetPrediction = fallDetected + 0.1f
                entry2.add(Entry(i.toFloat(), fallDetected))
                Log.d("TimeMapping", "Entry $i corresponds to ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentDateTime.atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli()))}")
                currentDateTime = currentDateTime.plusSeconds(12)
            }

        } else {
            Log.e("ChartError", "ppgDataList is empty, no entries created")
        }

        if (entry1.isEmpty() || entry2.isEmpty()) {
            Log.e("ChartError", "No entries in the LineDataSet")
        }

        // 앱1
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

        // 앱2
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

    private fun prepareChartData(data: LineData, lineChart: LineChart) {
        lineChart.data = data // LineData 전달
        lineChart.invalidate() // LineChart 갱신해 데이터 표시
    }
}
