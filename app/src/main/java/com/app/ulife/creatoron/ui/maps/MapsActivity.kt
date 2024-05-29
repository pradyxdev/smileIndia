/*
 * *
 *  * Created by Prady on 19/10/22, 4:40 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 19/10/22, 4:40 PM
 *
 */

package com.app.ulife.creatoron.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.ActivityMapsBinding
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.utils.GPSTracker
import com.app.ulife.creatoron.utils.hideKeyboard
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var preferenceManager: PreferenceManager
    private var lat = 0.0
    private var log = 0.0

    private var draggedLat = 0.0
    private var draggedLog = 0.0

    private var myCompleteAddress = ""

    companion object {
        const val REQUEST_CHECK_SETTINGS = 19053
        const val DEFAULT_ZOOM = 16.5f
        const val TAG = "SelectPlaceActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager.instance
        lat = intent.getDoubleExtra("lat", lat)
        log = intent.getDoubleExtra("log", log)
        draggedLat = lat
        draggedLog = log
//        Log.e(
//            "CurrentLoc ",
//            "" + lat + " // " + log + " // " + GPSTracker(applicationContext).latitude
//        )
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getAddress(lat, log)
    }

    private fun setupListeners() {
        binding.apply {
            btnSelectLoc.setOnClickListener {
                val intent = Intent()
                intent.putExtra("lat", "" + draggedLat)
                intent.putExtra("log", "" + draggedLog)
                intent.putExtra("fullAddress", "" + myCompleteAddress)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Add a marker in Sydney and move the camera
        val currentLocation = LatLng(lat, log)
        Log.e("CurrentLoc2 ", "$lat // $log /// $currentLocation")

        mMap.addMarker(
            MarkerOptions().position(currentLocation).title("My current location !")
                .icon(bitmapFromVector(applicationContext, R.drawable.ic_baseline_location_on_maps)).draggable(true)
        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        moveCamera(lat, log)

        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
                val message =
                    p0!!.position.latitude.toString() + "" + p0.position.longitude.toString()
                Log.d("_DRAG", message)
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.position, DEFAULT_ZOOM))
                val message =
                    arg0.position.latitude.toString() + "" + arg0.position.longitude.toString()
                Log.d(TAG + "_END", message)
                draggedLat = arg0.position.latitude
                draggedLog = arg0.position.longitude

                getAddress(draggedLat, draggedLog)
            }
        })

        binding.btnSearch.setOnClickListener {
            GPSTracker(applicationContext).getLatLongFromAddress(applicationContext, binding.etAddress.text.toString())
            hideKeyboard(binding.etAddress)

            mMap.clear()

            val searchedLocation = LatLng(Constants.searchedLat, Constants.searchedLog)
            mMap.addMarker(
                MarkerOptions().position(searchedLocation).title("Searched location !")
                    .icon(bitmapFromVector(applicationContext, R.drawable.ic_baseline_location_on_maps)).draggable(true)
            )
            draggedLat = Constants.searchedLat
            draggedLog = Constants.searchedLog
            getAddress(draggedLat, draggedLog)
            moveCamera(draggedLat, draggedLog)
        }
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /*
    Takes latitude and longitude and update the map with marker
     */
    private fun moveCamera(lat: Double, lng: Double) {
        //moving camera with animation
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lat,
                    lng
                ), DEFAULT_ZOOM
            )
        )
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(this, Locale.ENGLISH)
            val addressList = geocoder.getFromLocation(
                latitude, longitude,
                1
            )
            var list = mutableListOf<String>()

//            BuildingNumber = "" + addressList!![0].featureName
//            CityName = "" + addressList!![0].subAdminArea
//            StateName = "" + addressList!![0].adminArea
//            Pincode = "" + addressList!![0].postalCode
            myCompleteAddress = "" + addressList!![0].getAddressLine(0)
            preferenceManager.mapDraggedAddress = myCompleteAddress
            Log.v("myCompleteAddress ", "" + myCompleteAddress)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LOCATION ERROR: " , "" + e)
        }
    }
}