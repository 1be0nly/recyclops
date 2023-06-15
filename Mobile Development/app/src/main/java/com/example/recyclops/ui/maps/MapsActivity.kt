package com.example.recyclops.ui.maps

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.recyclops.R
import com.example.recyclops.data.TrashBankLocation

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.recyclops.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val indonesiaLocation = LatLng(-6.1753924, 106.8271528)
        val zoomLevel = 5.0f

        //       Ui Settings
        mMap.uiSettings.isZoomControlsEnabled = true

        //       Zoom in on Indonesia

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLocation, zoomLevel))

        //      Get the user's location if permission is granted
        getMyLocation()

        //      Add markers for each trash bank location
        dummyMarkerAroundMe()
    }

    //    Create the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    //    Handle the map type change
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (handleMapTypeChange(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //    Change the map type based on the user's selection
    private fun handleMapTypeChange(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> false
        }
    }

    //    Request permission to access the user's location
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    //    Get the user's location if permission is granted
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun dummyMarkerAroundMe() {
        val dummyLocation = listOf(
            // Jakarta
            TrashBankLocation("Trash Bank Jakarta 1", LatLng(-6.2087634, 106.845599)),
            TrashBankLocation("Trash Bank Jakarta 2", LatLng(-6.2234942, 106.8080538)),
            // Bandung
            TrashBankLocation("Trash Bank Bandung 1", LatLng(-6.903429, 107.618705)),
            TrashBankLocation("Trash Bank Bandung 2", LatLng(-6.916668, 107.619233)),
            // Surabaya
            TrashBankLocation("Trash Bank Surabaya 1", LatLng(-7.2574719, 112.7520883)),
            TrashBankLocation("Trash Bank Surabaya 2", LatLng(-7.2764426, 112.7914753)),
        )

        dummyLocation.forEach { bank ->
            mMap.addMarker(
                MarkerOptions()
                    .position(bank.latLng)
                    .title(bank.name)
            )
            boundsBuilder.include(bank.latLng)
        }
    }
}