package com.example.halifaxtransitapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.halifaxtransitapp.MainViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@Composable
fun MapUI(mainViewModel: MainViewModel){
    val mapViewportState = mainViewModel.mapViewportState

    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(14.0)
                center(Point.fromLngLat(-63.6027, 44.6544))
                pitch(0.0)
                bearing(0.0)
            }
        }
    )
}

