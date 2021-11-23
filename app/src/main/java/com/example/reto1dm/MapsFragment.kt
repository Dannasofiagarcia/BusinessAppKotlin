package com.example.reto1dm

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.fragment.app.setFragmentResult
import com.example.reto1dm.model.Publication
import com.google.gson.Gson
import java.util.*

private const val ARG_PARAM1 = "1"

class MapsFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentUsage: Int? = null
    private val LOCATION_SET = 0
    private val PUBLICATIONS_MAP = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentUsage = it.getInt(ARG_PARAM1)
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            val loc = LatLng(location!!.latitude, location!!.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        }
        when (this.currentUsage) {
            LOCATION_SET -> {
                googleMap.setOnMapClickListener { listener ->
                    val loc = LatLng(listener.latitude, listener.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(loc),
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val address = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)[0].getAddressLine(0)

                    setFragmentResult("newLoc", bundleOf("bundleKey" to address))
                    parentFragmentManager.popBackStack()
                }
            }
            PUBLICATIONS_MAP -> {
                var i = 0;
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                var json = sharedPref?.getString("publication$i", "NO_DATA")
                while(json != "NO_DATA") {
                    val deserialized = Gson().fromJson(json, Publication::class.java)
                    val result = Geocoder(requireContext(), Locale.getDefault())
                        .getFromLocationName(deserialized.ubication, 1)[0]
                    val loc = LatLng(result.latitude, result.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(loc)
                            .title(deserialized.profileName)
                            .snippet(deserialized.eventName)
                    )
                    i++
                    json = sharedPref?.getString("publication$i", "NO_DATA")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        @JvmStatic
        fun newInstance(currentUsage: Int) = MapsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, currentUsage)
            }
        }
    }
}