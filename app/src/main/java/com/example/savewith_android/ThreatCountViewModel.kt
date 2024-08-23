package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

data class ThreatCountInfo(
    val countFall: String,
    val countDanger: String,
    val countWay: String
)

@RequiresApi(Build.VERSION_CODES.O)
class ThreatCountViewModel(application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "incident-log-db").build()
    private val _incidentInfos = MutableLiveData<List<ThreatCountInfo>>()
    val incidentInfos: LiveData<List<ThreatCountInfo>> get() = _incidentInfos

    private var currentDate = LocalDate.now()
    private var countList = mutableListOf<ThreatCount>()


    init {
        initializeCountData()
        loadIncidentsForDate(currentDate)
    }

    private fun initializeCountData() {
        // 초기 데이터를 0으로 설정
        val initialCountData = ThreatCount(
            countFall = 0,
            countDanger = 0,
            countWay = 0
        )
        countList.add(initialCountData)
        processCountData(countList)
    }

    private fun loadIncidentsForDate(date: LocalDate) {
        /*Executors.newSingleThreadExecutor().execute {
            val startOfDay = date.atStartOfDay()
            val endOfDay = date.atTime(LocalTime.MAX)
            val incidentCounts = db.incidentLogDao().getIncidentCountsBetween(startOfDay, endOfDay)
            val incidentInfos = incidentCounts.map { IncidentInfo(it.type, it.count) }
            _incidentInfos.postValue(incidentInfos)
        }*/
        processCountData(countList)
    }

    private fun processCountData(threatCount : List<ThreatCount>){
        val threatCountInfos = threatCount.map { data ->
            val countFall = data.countFall.toString()
            val countDanger = data.countDanger.toString()
            val countWay = data.countWay.toString()

            ThreatCountInfo(countFall, countDanger, countWay)
        }
        _incidentInfos.postValue(threatCountInfos)
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadIncidentsForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadIncidentsForDate(currentDate)
    }

    fun calculatePpg(ppgPrediction: List<Float>?): String {
        return if (!ppgPrediction.isNullOrEmpty()) {
            val highThreshold = 0.74
            val highCount = ppgPrediction.count { predictionValue ->
                predictionValue >= highThreshold
            }
            val highPercentage = (highCount.toDouble() / ppgPrediction.size) * 100

            when {
                highPercentage >= 70 -> "높음"
                highPercentage >= 30 -> "보통"
                else -> "낮음"
            }
        } else {
            "데이터 없음" // predictions가 null이거나 비어있는 경우 반환할 기본값
        }
    }

    fun addCountData(ppgPrediction: List<Float>, accPredictionList: List<Float>) {
        val prediction = calculatePpg(ppgPrediction)
        val accPrediction = accPredictionList[1]

        var countFall = 0
        var countDanger = 0

        // 조건에 따라 countFall 또는 countDanger 값을 증가
        if (accPrediction == 2f) {
            countFall += 1
        }
        if (prediction == "높음" && accPrediction == 2f) {
            countDanger += 1
        }

        // countFall 또는 countDanger 값이 증가하면 countList에 반영
        if (countFall > 0 || countDanger > 0) {
            val currentCountData = if (countList.isEmpty()) {
                ThreatCount(0, 0, 0) // 기본값으로 초기화
            } else {
                countList.last()
            }

            val newCountData = ThreatCount(
                countFall = currentCountData.countFall + countFall,
                countDanger = currentCountData.countDanger + countDanger,
                countWay = currentCountData.countWay // countWay는 변경하지 않음
            )
            countList.add(newCountData)
            processCountData(countList)
        }

    }

}