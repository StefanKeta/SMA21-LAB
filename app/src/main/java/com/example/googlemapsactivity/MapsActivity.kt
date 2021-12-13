package com.example.googlemapsactivity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.googlemapsactivity.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val CODE = 1

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

        if(checkPermission()){
            mMap.isMyLocationEnabled = true
        }else{
            checkPermission()
        }
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val facultate = LatLng(45.7450284,21.2275766)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(facultate))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.toFloat()))
        drawPolyline(ArrayList<LatLng>().plus(facultate),mMap)

        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(marker: Marker): Boolean {
                if (marker.position.equals(facultate))
                    Toast.makeText(this@MapsActivity,"Studying",Toast.LENGTH_SHORT)
                        .show()
                return true
            }
        })
    }

    fun drawPolyline(list:List<LatLng>, map:GoogleMap){
        val polyOptions = PolylineOptions()
        polyOptions.color(Color.GREEN)
        polyOptions.width(8.0f)
        polyOptions.addAll(list)
        map.addPolyline(polyOptions)
        val builder = LatLngBounds.builder()
        for (latLng in list){
            builder.include(latLng)
        }
        builder.build()
    }

    private fun checkPermission():Boolean{
        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun askPermission(){
        val array : Array<String> = ArrayList<String>().toArray() as Array<String>
        array.plus(Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(array,CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(checkPermission())
                    mMap.isMyLocationEnabled = true
            }else{
                askPermission()
            }
        }
    }
}