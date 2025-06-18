package com.example.halifaxtransitapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.halifaxtransitapp.ui.screens.AlertsUI
import com.example.halifaxtransitapp.ui.screens.MapUI
import com.example.halifaxtransitapp.ui.theme.HalifaxTransitAppTheme
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

class MainActivity : ComponentActivity() {

    //define view model
    private lateinit var mainViewModel: MainViewModel

    //PermissionsManager for mapbox, researched from https://docs.mapbox.com/android/maps/guides/user-location/permissions/
    lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {

        //check if permissions granted via mapbox PermissionsManager
        if(PermissionsManager.areLocationPermissionsGranted(this)){

            Log.v("INFO", "Permissions granted")

            //permissions are granted
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {

                //init view model
                mainViewModel = viewModel()

                //load bus positions
                mainViewModel.loadBusPositions()

                //display the ui
                HalifaxTransitAppTheme {
                    DisplayUI(mainViewModel)
                }
            }

        } else {
            Log.v("INFO", "Permissions NOT granted")
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(this)
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(mainViewModel: MainViewModel){

    //nav controller
    val navController = rememberNavController()

    //selected index for nav bar
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Halifax Transit App")
                },
                //colours for top bar
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    label = { Text("Map") },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate("map")
                    },
                    icon = { Icon(
                        painter = painterResource(R.drawable.bus_icon),
                        contentDescription = "Map of transit area"
                    )
                    },
                )
                NavigationBarItem(
                    label = {Text("Alerts")},
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate("alerts")
                    },
                    icon = { Icon(
                        painter = painterResource(R.drawable.ic_alerts),
                        contentDescription = "Service alerts"
                    )
                    },
                )
            }
        }
    ){
            innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = "map"){
                    MapUI(mainViewModel)
            }
            composable(route = "alerts"){
                    AlertsUI(mainViewModel)
            }
        }
    }
}


//no logic requirements for the permissionsListener, just needed when constructing permissionsManager
//researched from https://docs.mapbox.com/android/maps/guides/user-location/permissions/
var permissionsListener: PermissionsListener = object : PermissionsListener {
    override fun onExplanationNeeded(permissionsToExplain: List<String>) {

    }
    override fun onPermissionResult(granted: Boolean) {

    }
}