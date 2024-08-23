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

data class ThreatLogInfo(
    val timestamp: String,
    val situation: String,
    val count: String
)
@RequiresApi(Build.VERSION_CODES.O)
class ThreatLogViewModel(application: Application) : AndroidViewModel(application) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "dangerous-situation-log-db").build()
    private val _threatLogInfos = MutableLiveData<List<ThreatLogInfo>>().apply {
        value = listOf(
            ThreatLogInfo("2024-08-18 13:19:53", "위험상황", "1"),
            ThreatLogInfo("2024-07-13 16:33:17", "위험상황", "1")
        ) // 초기값 설정
    }
    val threatLogInfos: LiveData<List<ThreatLogInfo>> get() = _threatLogInfos

    private var currentDate = LocalDate.now()
    private val threatLogList = mutableListOf<ThreatLogInfo>()

    init {
        loadDangerousSituations(currentDate)
    }

    private fun loadDangerousSituations(date: LocalDate) {
        /*Executors.newSingleThreadExecutor().execute {
            val logs = db.dangerousSituationLogDao().getAllDangerousSituations()
            val infos = logs.map { DangerousSituationInfo(it.timestamp, it.count) }
            _dangerousSituationInfos.postValue(infos)
        }*/
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadDangerousSituations(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadDangerousSituations(currentDate)
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
            "데이터 없음"
        }
    }

    fun addThreatLog(ppgPrediction: List<Float>, accPredictionList: List<Float>) {
        val prediction = calculatePpg(ppgPrediction)
        val accPrediction = accPredictionList[1]

        var situation: String? = null

        // accPrediction이 2인 경우 상황은 "낙상", count는 1 증가
        if (accPrediction == 2f) {
            situation = "낙상"
        }
        // prediction이 "높음"이면서 accPrediction이 2인 경우 상황은 "위험상황", count는 1 증가
        if (prediction == "높음" && accPrediction == 2f) {
            situation = "위험상황"
        }

        // 상황이 설정된 경우 로그를 추가
        if (situation != null) {
            val currentCountString = threatLogList.lastOrNull { it.situation == situation }?.count ?: "0"
            val currentCount = currentCountString.toIntOrNull() ?: 0

            val newCount = (currentCount + 1)

            val newThreatLog = ThreatLogInfo(
                timestamp = LocalDateTime.now().format(dateTimeFormatter),
                situation = situation,
                count = newCount.toString()
            )
            threatLogList.add(newThreatLog)
            _threatLogInfos.postValue(threatLogList)
        }
    }

}