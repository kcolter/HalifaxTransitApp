package com.example.halifaxtransitapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.transit.realtime.GtfsRealtime
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

//for SSL trust-all
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

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

        //WARNING: FOR DEV/DEBUG ONLY
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        //END DEV/DEBUG ONLY

        Log.v("INFO", "in loadBusPositions")

        viewModelScope.launch {
            try {
                val url = URL("https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")
                // Run code (which is blocking) on a background thread optimized for I/O,
                // and suspend the coroutine until it's done.
                val feed = withContext(Dispatchers.IO) {
                    GtfsRealtime.FeedMessage.parseFrom(url.openStream())
                }
                Log.v("INFO", feed.toString())
                _gtfs.value = feed
            } catch (e: Exception) {
                Log.v("INFO", e.toString() )
            }
        }
    }
}


/*
NOTE REGARDING SSL ERROR
was getting error javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.

in the interest of time [and this being an educational context] my workaround is to disable the SSL check, something obviously not tenable in an IRL production context
 */