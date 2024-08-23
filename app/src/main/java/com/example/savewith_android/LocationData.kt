package com.example.savewith_android

import android.location.Location
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
    val timestamp: LocalDateTime,
    val stressLevel: StressLevel
)

data class BpData(
    val systolic: Int,
    val diastolic: Int,
    val timestamp: LocalDateTime
)

data class DayEventData(
    val timestamp: LocalDateTime,
    val event: String,
    val prediction: Float,
    val fallDetected: Float
)

data class ThreatCount(
    val countFall: Int,
    val countDanger: Int,
    val countWay: Int
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

val locationList = mutableListOf<Location>()

object WebSocketDataManager {
    var xTestTwelveSec: List<Any?>? = emptyList()
    var predictions: List<Any?>? = emptyList()
    var ppgData: List<Float>? = emptyList()
    var accPredictions: List<Any?>? = emptyList()
    var accData: List<Any?>? = emptyList()
    var svmAccData: List<Any?>? = emptyList()
    var bpm: Int = 0
    var step: Int = 0

    fun updateData(data: Map<String, Any?>) {
        xTestTwelveSec = data["x_test_twelve_sec"] as? List<Any?>
        predictions = data["predictions"] as? List<Float>
        ppgData = data["ppg_data"] as? List<Float>
        accPredictions = data["acc_predictions"] as? List<Any?>
        accData = data["acc_data"] as? List<Any?>
        svmAccData = data["svm_acc_data"] as? List<Any?>
}}