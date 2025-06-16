package com.example.halifaxtransitapp

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.halifaxtransitapp.ui.screens.AlertsUI
import com.example.halifaxtransitapp.ui.screens.MapUI
import com.example.halifaxtransitapp.ui.theme.HalifaxTransitAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class MainActivity : ComponentActivity() {

    //define view model
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            //init view model
            mainViewModel = viewModel()

            //display the ui
            HalifaxTransitAppTheme {
              DisplayUI(mainViewModel)
            }
        }
    }
}


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
                        painter = painterResource(R.drawable.ic_placeholder),
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
                        painter = painterResource(R.drawable.ic_placeholder),
                        contentDescription = "weather alerts"
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
                if (true /*check if data != null*/) {
                    MapUI(mainViewModel)
                }
            }

            composable(route = "alerts"){
                if (true /*check if data != null*/) {
                    AlertsUI(mainViewModel)
                }
            }
        }
    }
}

@UnstableApi
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetLocation(
    //inject view model here so we can pass the coordinates to it
    viewModel: MainViewModel = viewModel()
) {
    // Remember the permission state(asking for Fine location)
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    if (permissionState.status.isGranted) {

        // Get Location
        val currentContext = LocalContext.current
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(currentContext)

        if (ContextCompat.checkSelfPermission(
                currentContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude.toString()
                        val lng = location.longitude.toString()

                        val coordinates = "$lat,$lng"

                        // call a function, like in View Model, to do something with location...
                        //pass coordinates into the viewModel
                        viewModel.updateLocation(coordinates)
                    }
                    else {
                        Log.i("TESTING", "Problem encountered: Location returned null")
                    }
                }
        }
    }
    else {
        // Run a side-effect (coroutine) to get permission. The permission popup.
        LaunchedEffect(permissionState){
            permissionState.launchPermissionRequest()
        }
    }
}
