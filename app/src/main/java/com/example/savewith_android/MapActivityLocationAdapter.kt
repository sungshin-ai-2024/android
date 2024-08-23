package com.example.savewith_android

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savewith_android.databinding.ItemLocationMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivityLocationAdapter (private val context: Context, private val locationList: List<Location>) :
    RecyclerView.Adapter<MapActivityLocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(val binding: ItemLocationMapBinding) :
        RecyclerView.ViewHolder(binding.root), OnMapReadyCallback {
        private lateinit var googleMap: GoogleMap

        init {
            binding.mapView.onCreate(null)
            binding.mapView.getMapAsync(this)
        }

        override fun onMapReady(googleMap: GoogleMap) {
            this.googleMap = googleMap
            val position = locationList[adapterPosition]
            val latLng = LatLng(position.latitude, position.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng).title("Location ${adapterPosition + 1}"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationMapBinding.inflate(LayoutInflater.from(context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.binding.mapView.tag = position
        holder.binding.mapView.getMapAsync(holder)
    }

    override fun getItemCount(): Int = locationList.size

    override fun onViewRecycled(holder: LocationViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.mapView.onDestroy()
    }
}