package com.example.carvalho.studios

import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.carvalho.studios.model.Studio
import com.example.carvalho.studios.util.Util
import com.google.android.gms.common.internal.DowngradeableSafeParcel.DowngradeableSafeParcelHelper.getParcelable

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var studio: Studio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment


        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.clear()

        if (Util.isNetworkAvailable(this)) {


            val geoCoder = Geocoder(this)
            var address: MutableList<Address>?

            studio = intent.getParcelableExtra<Studio>("studio")


            address = geoCoder.getFromLocationName(
                    studio.cep,
                    1

            )


            if (address.isNotEmpty()) {
                val location = address[0]
                if (address.size > 0) {
                    addMarcador(location.latitude, location.longitude, studio.nome)
                } else {
                    Log.d("map", getString(R.string.location_error))
                    Toast.makeText(this, R.string.location_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("map", getString(R.string.location_error))
                Toast.makeText(this, R.string.location_error, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.connection_not_accepted, Toast.LENGTH_SHORT).show()
        }
    }

    fun addMarcador(latitude: Double, longitude: Double, title : String) {
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions()
                .position(location)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_laucher_map)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16f))
    }
}
