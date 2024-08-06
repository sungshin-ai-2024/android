package com.example.savewith_android

import android.app.Application
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _locationTimeRanges = MutableLiveData<List<LocationTimeRange>>()
    val locationTimeRanges: LiveData<List<LocationTimeRange>> get() = _locationTimeRanges

    init {
        loadLocations()
    }

    private fun loadLocations() {
        // 예제 위치 데이터를 생성합니다.
        val sampleLocations = listOf(
            LocationData(37.5665, 126.9780, LocalDateTime.now().minusHours(5)),
            LocationData(37.5665, 126.9780, LocalDateTime.now().minusHours(3)),
            LocationData(37.5665, 126.9780, LocalDateTime.now().minusHours(1)),
            LocationData(37.5670, 126.9790, LocalDateTime.now().minusMinutes(30))
        )

        val targetLocations = listOf(
            Pair(37.5665, 126.9780) // 예: 서울의 특정 위치들
        )

        val timeRanges = targetLocations.map { (latitude, longitude) ->
            val timeRange = LocationUtils.calculateTimeRange(sampleLocations, latitude, longitude, 0.5)
            val locationName = fetchLocationName(latitude, longitude)
            LocationTimeRange(locationName, timeRange.first, timeRange.second)
        }

        _locationTimeRanges.value = timeRanges
    }

    private fun fetchLocationName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(getApplication(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            addresses[0].getAddressLine(0)
        } else {
            "Unknown location"
        }
    }
}