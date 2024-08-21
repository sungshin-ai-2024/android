package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class StressLevel {
    LOW, MEDIUM, HIGH
}


data class StressInfo(
    val startTime: String,
    val endTime: String,
    val lowCount: Int,
    val mediumCount: Int,
    val highCount: Int,
    val state: String
)

@RequiresApi(Build.VERSION_CODES.O)
class StressViewModel(application: Application) : AndroidViewModel(application) {
    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "stress-db").build()
    private val _stressInfos = MutableLiveData<List<StressInfo>>()
    val stressInfos: LiveData<List<StressInfo>> get() = _stressInfos

    private var currentDate = LocalDate.now()
    private val stressDataList = mutableListOf<StressData>()

    init {
        loadStressDataForDate(currentDate)
    }

    private fun loadStressDataForDate(date: LocalDate) {
        //Executors.newSingleThreadExecutor().execute {
            // 데이터베이스에서 특정 날짜의 스트레스 데이터를 가져옵니다.
            //val startOfDay = date.atStartOfDay()
            //val endOfDay = date.atTime(LocalTime.MAX)
        processStressData(stressDataList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processStressData(stressData: List<StressData>) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val groupedData = stressData.groupBy {
            it.timestamp.withSecond((it.timestamp.second / 12) * 12).withNano(0)
        }
        val stressInfos = groupedData.map { (startTime, data) ->
            val formattedStartTime = startTime.format(timeFormatter)
            val endTime = startTime.plusSeconds(12)
            val formattedEndTime = endTime.format(timeFormatter)
            val lowCount = data.count { it.stressLevel == StressLevel.LOW }
            val mediumCount = data.count { it.stressLevel == StressLevel.MEDIUM }
            val highCount = data.count { it.stressLevel == StressLevel.HIGH }
            val status = when {
                highCount > mediumCount && highCount > lowCount -> "높음"
                mediumCount > lowCount -> "중간"
                else -> "낮음"
            }
            StressInfo(formattedStartTime, formattedEndTime, lowCount, mediumCount, highCount, status)
        }
        _stressInfos.postValue(stressInfos)
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadStressDataForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadStressDataForDate(currentDate)
    }

    fun addStressData(prediction: Float) {
        val stressLevel = if (prediction >= 0.75) StressLevel.HIGH else StressLevel.LOW
        val newStressData = StressData(
            timestamp = LocalDateTime.now(),
            stressLevel = stressLevel
        )
        stressDataList.add(newStressData)
        processStressData(stressDataList)
    }
}