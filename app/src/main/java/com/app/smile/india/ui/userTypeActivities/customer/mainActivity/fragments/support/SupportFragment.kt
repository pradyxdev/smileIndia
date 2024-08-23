/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentSupportBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.ui.userTypeActivities.customer.CustMainActivity
import com.app.smile.india.viewModels.SharedVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SupportFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentSupportBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var lat: Double = 0.0
    private var log: Double = 0.0
    private lateinit var nMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        setupMap(26.8553724, 80.990323)
//        if (GPSTracker(activity).canGetLocation()) {
//            setupMap(26.8553724,80.990323)
//        } else {
//            GPSTracker(activity).showSettingsAlert()
//        }
    }

    override fun onResume() {
        super.onResume()
        (activity as CustMainActivity).setBottombarVisibility(isVisible = true)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun setupMap(latt: Double, longg: Double) {
        lat = latt
        log = longg

//        if (GPSTracker(context).canGetLocation()) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
//        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play Stations is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play Stations and returned to the app.
         */

        nMap = googleMap
        val userCurrentLocation = LatLng(lat, log)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userCurrentLocation))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, log), 13f))

//        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        // static vendors location
        googleMap.addMarker(
            MarkerOptions().position(LatLng(lat, log))
                .title("Smile India")
                .snippet("Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        googleMap.setOnMarkerClickListener {
            Log.e("stationMarks4 ", "" + it.title + ", " + it.snippet)
            // todo set ui

            true
        }
    }
}