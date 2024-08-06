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


data class BloodPressureInfo(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val minSystolic: Int,
    val maxSystolic: Int,
    val minDiastolic: Int,
    val maxDiastolic: Int
)

@RequiresApi(Build.VERSION_CODES.O)
class BpViewModel (application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "blood-pressure-db").build()
    private val _bloodPressureInfos = MutableLiveData<List<BloodPressureInfo>>()
    val bloodPressureInfos: LiveData<List<BloodPressureInfo>> get() = _bloodPressureInfos

    private var currentDate = LocalDate.now()

    init {
        loadBloodPressureDataForDate(currentDate)
    }

    private fun loadBloodPressureDataForDate(date: LocalDate) {
        /*Executors.newSingleThreadExecutor().execute {
            // 데이터베이스에서 특정 날짜의 혈압 데이터를 가져옵니다.
            val startOfDay = date.atStartOfDay()
            val endOfDay = date.atTime(LocalTime.MAX)
            val bloodPressureData = db.bloodPressureDataDao().getBloodPressureDataBetween(startOfDay, endOfDay)
            processBloodPressureData(bloodPressureData)
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processBloodPressureData(bloodPressureData: List<BpData>) {
        val groupedData = bloodPressureData.groupBy {
            it.timestamp.withMinute((it.timestamp.minute / 30) * 30).withSecond(0).withNano(0)
        }
        val bloodPressureInfos = groupedData.map { (startTime, data) ->
            val endTime = startTime.plusMinutes(30)
            val minSystolic = data.minByOrNull { it.systolic }?.systolic ?: 0
            val maxSystolic = data.maxByOrNull { it.systolic }?.systolic ?: 0
            val minDiastolic = data.minByOrNull { it.diastolic }?.diastolic ?: 0
            val maxDiastolic = data.maxByOrNull { it.diastolic }?.diastolic ?: 0
            BloodPressureInfo(startTime, endTime, minSystolic, maxSystolic, minDiastolic, maxDiastolic)
        }
        _bloodPressureInfos.postValue(bloodPressureInfos)
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadBloodPressureDataForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadBloodPressureDataForDate(currentDate)
    }
}
