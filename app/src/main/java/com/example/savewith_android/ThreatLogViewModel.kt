package com.example.savewith_android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime

data class ThreatLogInfo(
    val timestamp: LocalDateTime,
    val count: Int
)

class ThreatLogViewModel(application: Application) : AndroidViewModel(application) {

    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "dangerous-situation-log-db").build()
    private val _threatLogInfos = MutableLiveData<List<ThreatLogInfo>>()
    val threatLogInfos: LiveData<List<ThreatLogInfo>> get() = _threatLogInfos

    init {
        loadDangerousSituations()
    }

    private fun loadDangerousSituations() {
        /*Executors.newSingleThreadExecutor().execute {
            val logs = db.dangerousSituationLogDao().getAllDangerousSituations()
            val infos = logs.map { DangerousSituationInfo(it.timestamp, it.count) }
            _dangerousSituationInfos.postValue(infos)
        }*/
    }
}