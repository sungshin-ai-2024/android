package com.example.savewith_android

import com.github.mikephil.charting.data.LineData
import java.time.LocalDateTime

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timestamp: LocalDateTime
)

data class LocationTimeRange(
    val locationName: String,
    val firstTime: LocalDateTime?,
    val lastTime: LocalDateTime?
)

data class HrData(
    val heartRate: Int,
    val timestamp: LocalDateTime
)

data class StressData(
    val state: String,
    val timestamp: LocalDateTime
)

data class BpData(
    val systolic: Int,
    val diastolic: Int,
    val timestamp: LocalDateTime
)

data class DayEventData(
    val event: String,
    val timestamp: LocalDateTime
)

data class ThreatCount(
    val type: String,
    val count: Int
)

data class ThreatLog(
    val timestamp: LocalDateTime,
    val count: Int // 해당 날짜의 위험 상황 발생 횟수
)

data class Contact(
    val name: String,
    val phoneNumber: String
)

data class BpChartData(val date: String, val chartData: LineData)