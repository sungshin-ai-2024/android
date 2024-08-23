package com.example.savewith_android

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

class StressChartAdapter (private val chartDataList: List<BpChartData>) : RecyclerView.Adapter<StressChartAdapter.ChartViewHolder>() {

    class ChartViewHolder(val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ItemChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val item = chartDataList[position]

        // 데이터로부터 최소값과 최대값 계산
        val entryList = mutableListOf<Entry>()
        for (dataSet in item.chartData.dataSets) {
            if (dataSet is LineDataSet) {
                for (i in 0 until dataSet.entryCount) {
                    entryList.add(dataSet.getEntryForIndex(i))
                }
            }
        }

        val minValue = entryList.minOfOrNull { it.y } ?: 0f  // 데이터가 없을 경우 기본값 0f
        val maxValue = entryList.maxOfOrNull { it.y } ?: 0f


        holder.binding.itemLineChart.data = item.chartData
        holder.binding.itemLineChart.data.notifyDataChanged()

        holder.binding.itemLineChart.notifyDataSetChanged()
        holder.binding.itemLineChart.moveViewToX(entryList.size.toFloat())

        holder.binding.itemLineChart.invalidate() // 차트 새로고침
        holder.binding.itemLineChart.moveViewToAnimated(entryList.size.toFloat(),0f, YAxis.AxisDependency.LEFT, 500)

        configureChartAppearance(holder.binding.itemLineChart, minValue, maxValue) // 차트의 모양 설정
    }

    private fun configureChartAppearance(lineChart: LineChart, minValue: Float, maxValue: Float) {
        lineChart.extraBottomOffset = 15f // 간격
        lineChart.description.isEnabled = false // chart 밑에 description 표시 유무
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // Legend는 차트의 범례
        val legend = lineChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 10f
        legend.textSize = 13f
        legend.textColor = Color.parseColor("#242424")
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
        yAxisLeft.textSize = 10f
        yAxisLeft.textColor = Color.rgb(163, 163, 163)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.axisLineWidth = 0.5f
        yAxisLeft.axisMinimum = minValue // 최솟값
        yAxisLeft.axisMaximum = maxValue // 최댓값
        yAxisLeft.granularity = 10f

        // YAxis(Left) (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        val yAxis = lineChart.axisRight
        yAxis.setDrawLabels(false) // label 삭제
        yAxis.textColor = Color.rgb(163, 163, 163)
        yAxis.setDrawAxisLine(false)
        yAxis.axisLineWidth = 0.5f
        yAxis.axisMinimum = minValue // 최솟값
        yAxis.axisMaximum = maxValue // 최댓값
        yAxis.granularity = 10f as Float
        yAxis.isEnabled = false

        // XAxis에 원하는 String 설정하기 (날짜)
        // 12초 단위로 출력
        xAxis.valueFormatter = object : ValueFormatter() {
            @SuppressLint("DefaultLocale")
            override fun getFormattedValue(value: Float): String {
                // value는 x축의 index입니다. 이를 기반으로 12초씩 더해진 시간을 계산합니다.
                val baseTimeMillis = System.currentTimeMillis() // 기준 시간
                val displayedTime = baseTimeMillis + (value * 100).toLong() // 12초 단위로 증가
                return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(displayedTime))
            }
        }
    }

    override fun getItemCount() = chartDataList.size
}