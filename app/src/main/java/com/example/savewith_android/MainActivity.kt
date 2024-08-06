package com.example.savewith_android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lineChart: LineChart = binding.mainGraphAll
        configureChartAppearance(lineChart)
        var chartdata = createChartData().apply {
            prepareChartData(this, lineChart)
        }

        val pro_stress : CircleProgressView = binding.cpbCirclebar


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
        val timeFormat = SimpleDateFormat("a h:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())
        binding.tvMainDate.text = currentdate
        binding.tvMainTime.text = currentTime
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // 액티비티가 종료될 때 핸들러 콜백 제거
    }

    private fun configureChartAppearance(lineChart: LineChart) {
        lineChart.extraBottomOffset = 15f // 간격
        lineChart.description.isEnabled = false // chart 밑에 description 표시 유무

        // Legend는 차트의 범례
        val legend = lineChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 10f
        legend.textSize = 13f
        legend.textColor = Color.parseColor("#A3A3A3")
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.yEntrySpace = 5f
        legend.isWordWrapEnabled = true
        legend.xOffset = 80f
        legend.yOffset = 20f
        legend.calculatedLineSizes

        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        val xAxis = lineChart.xAxis
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM // x축 데이터 표시 위치
        xAxis.granularity = 1f
        xAxis.textSize = 14f
        xAxis.textColor = Color.rgb(118, 118, 118)
        xAxis.spaceMin = 0.1f // Chart 맨 왼쪽 간격 띄우기
        xAxis.spaceMax = 0.1f // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.textSize = 14f
        yAxisLeft.textColor = Color.rgb(163, 163, 163)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.axisLineWidth = 2f
        yAxisLeft.axisMinimum = 0f // 최솟값
        yAxisLeft.axisMaximum = 150f // 최댓값
        yAxisLeft.granularity = 10f

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        val yAxis = lineChart.axisRight
        yAxis.setDrawLabels(false) // label 삭제
        yAxis.textColor = Color.rgb(163, 163, 163)
        yAxis.setDrawAxisLine(false)
        yAxis.axisLineWidth = 2f
        yAxis.axisMinimum = 0f // 최솟값
        yAxis.axisMaximum = 150f // 최댓값
        yAxis.granularity = 10f as Float

        // XAxis에 원하는 String 설정하기 (날짜)
        xAxis.valueFormatter = object : ValueFormatter() {
            @SuppressLint("DefaultLocale")
            override fun getFormattedValue(value: Float): String {
                val hour = value.toInt()
                return String.format("%02d:00", hour)
            }
        }
    }

    private fun createChartData(): LineData {

        val Infos: List<String> = listOf("스트레스", "심박수")

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

        // 앱1
        val lineDataSet1 = LineDataSet(entry1, Infos.get(0))
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
        val lineDataSet2 = LineDataSet(entry2, Infos.get(1))
        chartData.addDataSet(lineDataSet2)

        lineDataSet2.lineWidth = 3f
        lineDataSet2.circleRadius = 6f
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setDrawCircleHole(true)
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.setDrawHorizontalHighlightIndicator(false)
        lineDataSet2.setDrawHighlightIndicators(false)
        lineDataSet2.color = Color.rgb(218, 30, 98)
        lineDataSet2.setCircleColor(Color.rgb(218, 30, 98))

        chartData.setValueTextSize(15f)
        return chartData
    }

    private fun prepareChartData(data: LineData, lineChart: LineChart) {
        lineChart.data = data // LineData 전달
        lineChart.invalidate() // LineChart 갱신해 데이터 표시
    }
}
