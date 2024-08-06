package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime

data class EventLogInfo(
    val time: LocalDateTime,
    val event: String
)

@RequiresApi(Build.VERSION_CODES.O)
class DayViewModel (application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "event-log-db").build()
    private val _eventLogInfos = MutableLiveData<List<EventLogInfo>>()
    val eventLogInfos: LiveData<List<EventLogInfo>> get() = _eventLogInfos


    private var currentDate = LocalDate.now()

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
    }

    private fun processEventLogs(eventLogs: List<DayEventData>) {
        val eventLogInfos = eventLogs.map {
            EventLogInfo(it.timestamp, it.event)
        }
        _eventLogInfos.postValue(eventLogInfos)
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadEventLogsForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadEventLogsForDate(currentDate)
    }
}