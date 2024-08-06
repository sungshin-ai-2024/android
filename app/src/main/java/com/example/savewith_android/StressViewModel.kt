package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.concurrent.fixedRateTimer

data class StressInfo(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
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

    init {
        loadStressDataForDate(LocalDate.now())
    }

    private fun loadStressDataForDate(date: LocalDate) {
        //Executors.newSingleThreadExecutor().execute {
            // 데이터베이스에서 특정 날짜의 스트레스 데이터를 가져옵니다.
            //val startOfDay = date.atStartOfDay()
            //val endOfDay = date.atTime(LocalTime.MAX)
        //val stressData = db.stressDataDao().getAllStressData()
        //processStressData(stressData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processStressData(stressData: List<StressData>) {
        val groupedData = stressData.groupBy {
            it.timestamp.withMinute((it.timestamp.minute / 30) * 30).withSecond(0).withNano(0)
        }
        val stressInfos = groupedData.map { (startTime, data) ->
            val endTime = startTime.plusMinutes(30)
            //val lowCount = data.count { it.stressLevel == StressLevel.LOW }
            //val mediumCount = data.count { it.stressLevel == StressLevel.MEDIUM }
            //val highCount = data.count { it.stressLevel == StressLevel.HIGH }
            //StressInfo(startTime, endTime, lowCount, mediumCount, highCount)
            //state 계산하는거 추가
        }
        //_stressInfos.postValue(stressInfos)
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadStressDataForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadStressDataForDate(currentDate)
    }
}