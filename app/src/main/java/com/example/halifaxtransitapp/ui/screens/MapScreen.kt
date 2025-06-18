package com.example.halifaxtransitapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.halifaxtransitapp.MainViewModel
import com.example.halifaxtransitapp.R
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun MapUI(mainViewModel: MainViewModel) {

    val gtfsFeed by mainViewModel.gtfs_bus.collectAsState()
    val entities = gtfsFeed?.entityList

    val mapViewportState = mainViewModel.mapViewportState

    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = mapViewportState
    )
    {

        //if statement for if location-permission is NOT granted
        if (!PermissionsManager.areLocationPermissionsGranted(LocalContext.current)) //LocalContext researched from https://stackoverflow.com/questions/58743541/how-to-get-context-in-jetpack-compose
            LaunchedEffect(Unit) {
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

        Log.v("INFO", "before entities forEach in map")
        //regardless of location-permissions, plot buses on map using ViewAnnotation
        entities?.forEach { entity ->
            val lon = entity.vehicle.position.longitude
            val lat = entity.vehicle.position.latitude
            val route = entity.vehicle.trip.routeId

            ViewAnnotation(
                options = viewAnnotationOptions {
                    //place at appropriate location
                    geometry(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
                }
            ) {
                //insert Bus with given route
                Bus(route)
            }

        }
    }
}


//view annotation component for our buses
@Composable
fun Bus(routeId: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(Color.Gray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bus_icon),
            contentDescription = "Bus with route ID $routeId",
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = routeId,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = Color.Yellow,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

