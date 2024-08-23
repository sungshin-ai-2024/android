package com.example.savewith_android

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

data class HeartRateInfo(
    val startTime: String,
    val endTime: String,
    val heartRate: String,
)

@RequiresApi(Build.VERSION_CODES.O)
class HrViewModel (application: Application) : AndroidViewModel(application) {

    private val _heartRateInfos = MutableLiveData<List<HeartRateInfo>>()
    val heartRateInfos: LiveData<List<HeartRateInfo>> get() = _heartRateInfos

    private var currentDate = LocalDate.now()
    private val hrDataList = mutableListOf<HrData>()

    init {
        startHeartRateMonitoring(currentDate)
    }

    private val heartRateList = mutableListOf<HrData>()
    private var lastHrData: HrData? = null


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startHeartRateMonitoring(date: LocalDate) {
        processHeartRateData(hrDataList)
    }

    private fun processHeartRateData(hrData: List<HrData>) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val heartRateInfos = hrData.map { data ->
            val startTime = data.timestamp.withSecond((data.timestamp.second / 12) * 12).withNano(0)
            val formattedStartTime = startTime.format(timeFormatter)
            val endTime = startTime.plusSeconds(12)
            val formattedEndTime = endTime.format(timeFormatter)

            val heartRate = data.heartRate.toString()  // 각 데이터의 heartRate 값을 그대로 사용

            HeartRateInfo(formattedStartTime, formattedEndTime, heartRate)
        }

        _heartRateInfos.postValue(heartRateInfos)
    }


    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        startHeartRateMonitoring(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        startHeartRateMonitoring(currentDate)
    }

    fun addHrData(bpm: Int) {
        val currentTime = LocalDateTime.now()

        // 중복 방지: 마지막으로 추가된 데이터와 새로 들어오는 데이터를 비교
        if (lastHrData == null || lastHrData?.heartRate != bpm || lastHrData?.timestamp?.plusSeconds(12)?.isBefore(currentTime) == true) {
            val newHrData = HrData(
                timestamp = currentTime,
                heartRate = bpm
            )
            hrDataList.add(newHrData)
            lastHrData = newHrData // 마지막으로 추가된 데이터 업데이트
            processHeartRateData(hrDataList)
        } else {
            Log.d("HrViewModel", "중복 데이터 추가 방지: 동일한 데이터가 이미 존재합니다.")
        }
    }
}