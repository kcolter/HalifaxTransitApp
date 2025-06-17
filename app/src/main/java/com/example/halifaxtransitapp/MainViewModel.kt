package com.example.halifaxtransitapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.transit.realtime.GtfsRealtime
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

//issues with these 3 imports auto-detecting, researched via giving the  codeblock on d2l to GPT and asking for import statements
import java.net.URL
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    // Store the map viewport state in the ViewModel so does not reset when screen is reloaded
    val mapViewportState = MapViewportState()

    private val _gtfs = MutableStateFlow<GtfsRealtime.FeedMessage?>(null)
    val gtfs = _gtfs.asStateFlow()

    // Get the real-time bus positions from Halifax Transit.
    fun loadBusPositions() {
        viewModelScope.launch {
            try {
                val url = URL("https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")
                // Run code (which is blocking) on a background thread optimized for I/O,
                // and suspend the coroutine until it's done.
                val feed = withContext(Dispatchers.IO) {
                    GtfsRealtime.FeedMessage.parseFrom(url.openStream())
                }
                Log.d("TESTING", feed.toString())
                _gtfs.value = feed
            } catch (e: Exception) {
                Log.e("TESTING", e.toString() )
            }
        }
    }
}