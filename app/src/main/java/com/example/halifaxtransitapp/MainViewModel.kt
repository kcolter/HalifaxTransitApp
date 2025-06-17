package com.example.halifaxtransitapp

import androidx.lifecycle.ViewModel
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

class MainViewModel : ViewModel() {

    var _longAndLat = ""

    // Store the map viewport state in the ViewModel so does not reset when screen is reloaded
    val mapViewportState = MapViewportState()


}