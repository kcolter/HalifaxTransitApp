package com.example.halifaxtransitapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.halifaxtransitapp.MainViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@Composable
fun MapUI(mainViewModel: MainViewModel){
    val mapViewportState = mainViewModel.mapViewportState

    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = mapViewportState
    )
//    {
//
//        //if statement for if location-permission is not granted
//        LaunchedEffect(Unit){
//            mapViewportState.flyTo(
//                cameraOptions = CameraOptions.Builder() //options as done in class
//                    .center(Point.fromLngLat(-63.6027, 44.6544)) // Halifax city center
//                    .zoom(14.0)
//                    .pitch(0.0)
//                    .bearing(0.0)
//                    .build()
//            )
//        }
//    }
}

