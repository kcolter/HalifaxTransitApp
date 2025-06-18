package com.example.halifaxtransitapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.halifaxtransitapp.MainViewModel
import com.google.transit.realtime.GtfsRealtime

@Composable
fun AlertsUI(mainViewModel: MainViewModel) {

    //get data from view model
    val gtfsFeed by mainViewModel.gtfs_alerts.collectAsState()
    val alerts = gtfsFeed?.entityList

    Spacer(modifier = Modifier.height(30.dp))

    if (alerts != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(alerts) { a ->
                Alert(a.alert)
            }
        }
    } else {
        Text("No alerts in Halifax at the present time.")
    }
}

@Composable
fun Alert(entity: GtfsRealtime.Alert) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(3.dp, MaterialTheme.colorScheme.secondary, RectangleShape)
            .padding(all = 10.dp)
            .fillMaxSize()
    ){
        Text (
            text = "Affected Route: " + entity.informedEntityList.first().routeId, //note: format is such that the entityList will be each stop along the route, but for our purposes just concerned with the route_id for now
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Cause: " + entity.cause
        )

        Text(
            text = "Effect: " + entity.effect
        )
        Text(
            text = entity.headerText.translationList.first().text //only 1 language [english] in Halifax
        )
    }
}