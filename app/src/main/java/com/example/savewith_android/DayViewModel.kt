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

data class EventLogInfo(
    val time: String,
    val event: String
)



@RequiresApi(Build.VERSION_CODES.O)
class DayViewModel (application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "event-log-db").build()
    private val _eventLogInfos = MutableLiveData<List<EventLogInfo>>()
    val eventLogInfos: LiveData<List<EventLogInfo>> get() = _eventLogInfos

    private var currentDate = LocalDate.now()
    private val dayList = mutableListOf<DayEventData>()

    init {
        loadEventLogsForDate(currentDate)
    }

    private fun loadEventLogsForDate(date: LocalDate) {
        /*Executors.newSingleThreadExecutor().execute {
            // 데이터베이스에서 특정 날짜의 이벤트 로그 데이터를 가져옵니다.
            val startOfDay = date.atStartOfDay()
            val endOfDay = date.atTime(LocalTime.MAX)
            val eventLogs = db.eventLogDao().getEventLogsBetween(startOfDay, endOfDay)
            processEventLogs(eventLogs)
        }*/
        processEventLogs(dayList)
    }

    private fun processEventLogs(eventLogs: List<DayEventData>) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val eventLogInfos = eventLogs.map {
            EventLogInfo(it.timestamp.format(timeFormatter), determineEventType(it.prediction, it.fallDetected))
        }
        _eventLogInfos.postValue(eventLogInfos)
    }

    private fun determineEventType(prediction: Float, fallDetected: Float): String {
        return when {
            prediction == 0F || fallDetected != 2F -> "스트레스 지수가 낮습니다."
            fallDetected == 0F || fallDetected == 1F -> "활동 중 입니다."
            fallDetected == 2F -> "낙상이 감지되었습니다."
            else -> "정상 상태입니다."
        }
    }


    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadEventLogsForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadEventLogsForDate(currentDate)
    }

    private var lastAddedData: DayEventData? = null
    private val minimumIntervalBetweenEvents = 500L

    fun addDayData(prediction: Float, fallDetected: Float) {
        Log.d("DayViewModel", "addDayData called with prediction: $prediction, fallDetected: $fallDetected")
        val eventType = determineEventType(prediction, fallDetected)
        val newDayData = DayEventData(
            timestamp = LocalDateTime.now(),
            event = eventType,
            prediction = prediction,
            fallDetected = fallDetected
        )
        if (lastAddedData == null || !isDuplicateData(newDayData)) {
            lastAddedData = newDayData
            dayList.add(newDayData)
            processEventLogs(dayList)
            Log.d("DayViewModel", "addDayData called with prediction: $prediction, fallDetected: $fallDetected")
        } else {
            Log.d("DayViewModel", "Duplicate data detected, skipping: prediction: $prediction, fallDetected: $fallDetected")
        }
    }

    private fun isDuplicateData(newData: DayEventData): Boolean {
        val lastData = lastAddedData ?: return false
        val timeDifference = java.time.Duration.between(lastData.timestamp, newData.timestamp).toMillis()
        return lastData.prediction == newData.prediction &&
                lastData.fallDetected == newData.fallDetected &&
                timeDifference < minimumIntervalBetweenEvents
    }
}