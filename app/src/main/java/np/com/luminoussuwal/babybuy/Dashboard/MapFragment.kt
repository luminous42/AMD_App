package np.com.luminoussuwal.babybuy.Dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MyMapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var selectedLocation: LatLng? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        getMapAsync(this)
        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set default location (adjust as needed)
        val defaultLocation = LatLng(27.7172, 85.3240) // Kathmandu, Nepal
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        // Add marker on map click
        googleMap.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            googleMap.clear() // Clear previous markers
            googleMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    // Function to get the selected location
    fun getSelectedLocation(): LatLng? {
        return selectedLocation
    }
}