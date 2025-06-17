package com.example.halifaxtransitapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.halifaxtransitapp.MainViewModel
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location


lateinit var permissionsManager: PermissionsManager

@Composable
fun MapUI(mainViewModel: MainViewModel){
    val mapViewportState = mainViewModel.mapViewportState

    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = mapViewportState
    )
    {

        //if statement for if location-permission is NOT granted
        if(!PermissionsManager.areLocationPermissionsGranted(LocalContext.current)) //LocalContext researched from https://stackoverflow.com/questions/58743541/how-to-get-context-in-jetpack-compose
        LaunchedEffect(Unit){
            mapViewportState.flyTo(
                cameraOptions = CameraOptions.Builder() //options as done in class
                    .center(Point.fromLngLat(-63.6027, 44.6544)) // Halifax city center
                    .zoom(14.0)
                    .pitch(0.0)
                    .bearing(0.0)
                    .build()
            )
        } else { //if perms are granted
            MapEffect(Unit) { mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }
                mapViewportState.transitionToFollowPuckState()
            }
        }
    }
}

