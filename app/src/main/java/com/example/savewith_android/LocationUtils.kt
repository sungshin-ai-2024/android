package com.example.savewith_android

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object LocationUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeRange(locations: List<LocationData>, targetLatitude: Double, targetLongitude: Double, radius: Double): Pair<LocalDateTime?, LocalDateTime?> {
        val targetLocation = LocationData(targetLatitude, targetLongitude, LocalDateTime.now())
        val filteredLocations = locations.filter { isWithinRadius(it, targetLocation, radius) }

        if (filteredLocations.isEmpty()) return Pair(null, null)

        val firstTime = filteredLocations.minByOrNull { it.timestamp }?.timestamp
        val lastTime = filteredLocations.maxByOrNull { it.timestamp }?.timestamp

        return Pair(firstTime, lastTime)
    }

    private fun isWithinRadius(location: LocationData, target: LocationData, radius: Double): Boolean {
        val distance = haversine(location.latitude, location.longitude, target.latitude, target.longitude)
        return distance <= radius
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6372.8 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.pow(Math.sin(dLat / 2), 2.0) + Math.pow(Math.sin(dLon / 2), 2.0) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        val c = 2 * Math.asin(Math.sqrt(a))
        return R * c
    }
}