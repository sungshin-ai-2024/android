package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

data class HeartRateInfo(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val minHeartRate: Int,
    val maxHeartRate: Int
)

@RequiresApi(Build.VERSION_CODES.O)
class HrViewModel (application: Application) : AndroidViewModel(application) {

    private val _heartRates = MutableLiveData<List<HrData>>()
    val heartRates: LiveData<List<HrData>> get() = _heartRates

    private val _heartRateInfos = MutableLiveData<List<HeartRateInfo>>()
    val heartRateInfos: LiveData<List<HeartRateInfo>> get() = _heartRateInfos

    private var currentDate = LocalDate.now()

    init {
        startHeartRateMonitoring(currentDate)
    }

    private val heartRateList = mutableListOf<HrData>()
    private var timer: Timer? = null


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startHeartRateMonitoring(date: LocalDate) {
        timer = fixedRateTimer("heartRateTimer", true, 0L, 1000L) {
            val heartRate = (60..100).random() // 스마트워치로부터 심박수 데이터 수신
            val currentTime = LocalDateTime.now()
            val heartRateData = HrData(heartRate, currentTime)
            heartRateList.add(heartRateData)
            _heartRates.postValue(heartRateList)
            processHeartRateData()
        }
    }

    private fun processHeartRateData() {
        val groupedData = heartRateList.groupBy {
            it.timestamp.withMinute((it.timestamp.minute / 30) * 30).withSecond(0).withNano(0)
        }
        val heartRateInfos = groupedData.map { (startTime, data) ->
            val endTime = startTime.plusMinutes(30)
            val minHeartRate = data.minByOrNull { it.heartRate }?.heartRate ?: 0
            val maxHeartRate = data.maxByOrNull { it.heartRate }?.heartRate ?: 0
            HeartRateInfo(startTime, endTime, minHeartRate, maxHeartRate)
        }
        _heartRateInfos.postValue(heartRateInfos)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        startHeartRateMonitoring(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        startHeartRateMonitoring(currentDate)
    }
}