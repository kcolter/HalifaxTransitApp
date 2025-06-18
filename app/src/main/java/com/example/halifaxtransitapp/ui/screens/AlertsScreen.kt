package com.example.halifaxtransitapp.ui.screens

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.halifaxtransitapp.MainViewModel

@Composable
fun AlertsUI(mainViewModel: MainViewModel){

    //get data from view model
    val gtfsFeed by mainViewModel.gtfs.collectAsState()
    Log.v("INFO", gtfsFeed.toString())
    //val alerts = gtfsFeed?.

    Text("ALERTS SCREEN")    //for testing
}