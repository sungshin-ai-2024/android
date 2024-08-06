package com.example.savewith_android

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

data class ThreatCountInfo(
    val type: String,
    val count: Int
)

@RequiresApi(Build.VERSION_CODES.O)
class ThreatCountViewModel(application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "incident-log-db").build()
    private val _incidentInfos = MutableLiveData<List<ThreatCountInfo>>()
    val incidentInfos: LiveData<List<ThreatCountInfo>> get() = _incidentInfos


    private var currentDate = LocalDate.now()

    init {
        loadIncidentsForDate(currentDate)
    }

    private fun loadIncidentsForDate(date: LocalDate) {
        /*Executors.newSingleThreadExecutor().execute {
            val startOfDay = date.atStartOfDay()
            val endOfDay = date.atTime(LocalTime.MAX)
            val incidentCounts = db.incidentLogDao().getIncidentCountsBetween(startOfDay, endOfDay)
            val incidentInfos = incidentCounts.map { IncidentInfo(it.type, it.count) }
            _incidentInfos.postValue(incidentInfos)
        }*/
    }

    fun loadPreviousDayData() {
        currentDate = currentDate.minusDays(1)
        loadIncidentsForDate(currentDate)
    }

    fun loadNextDayData() {
        currentDate = currentDate.plusDays(1)
        loadIncidentsForDate(currentDate)
    }
}