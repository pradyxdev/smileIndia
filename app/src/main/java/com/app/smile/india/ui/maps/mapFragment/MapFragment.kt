/*
 * *
 *  * Created by Prady on 6/3/23, 11:44 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 6/3/23, 11:10 AM
 *
 */

package com.app.smile.india.ui.maps.mapFragment

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentMapBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.utils.GPSTracker
import com.app.smile.india.viewModels.SharedVM
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom

class MapFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentMapBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    private var lat: Double = 0.0
    private var log: Double = 0.0
    private lateinit var nMap: GoogleMap
    val arrayMarker: MutableList<Marker> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
        hitApis()
    }

    private fun setupViews() {
        setHasOptionsMenu(true)
        preferenceManager.loggedIn = "true"
//        (activity as MainActivity?)?.setToolbarIcon(R.drawable.box_location)
//        (activity as MainActivity?)?.setToolbarTitle(GPSTracker(context).getLocality(context))
        setupMap()
    }

    private fun setupMap() {
        lat = GPSTracker(context).latitude
        log = GPSTracker(context).longitude

        if (GPSTracker(context).canGetLocation()) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnCloseDetailsCard.setOnClickListener {
                containerCardsDetails.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                containerCardsDetails.visibility = View.GONE
            }
        }
    }

    private fun hitApis() {
//        for (i in 0 until MainActivity.customers.size()) {
//            val customer: Customer = MainActivity.customers.get(i)
//            if (customer.getLon() !== 0.0) {
//                if (!customer.isProspect()) {
//                    val mData = Data(
//                        customer.getLat(), customer.getLon(), customer.getName(),
//                        customer.getOrt()
//                    )
//                    val location = LatLng(mData.lat, mData.lng)
//                     nMap.addMarker(
//                        MarkerOptions().position(location)
//                            .title(mData.title)
//                            .snippet(mData.snippet)
//                    )
//                    mMarkerArray.add(marker)
//                }
//            }
//        }

// todo to remove all markers
//        for (marker in mMarkerArray) {
//            marker.isVisible = false
//            //marker.remove(); <-- works too!
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

        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        // static vendors location
        val vendorLocation = LatLng(26.85559970231249, 80.9903694545835)
        googleMap.addMarker(
            MarkerOptions().position(vendorLocation).title("Vendor").snippet("Station name").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.85907417897957, 80.9847690021838)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.84776020189821, 80.98723663445321)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.870348742980052, 81.00131286730465)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.872186256385632, 80.97522033811663)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.86353437010709, 81.0108400736858)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.863151528116397, 80.9636331951122)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
        googleMap.addMarker(
            MarkerOptions().position(LatLng(26.84530964849142, 81.00938095189363)).title("Vendor")
                .snippet("Station name")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        googleMap.setOnMarkerClickListener {
            binding.containerCardsDetails.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            binding.containerCardsDetails.visibility = View.VISIBLE
            true
        }
    }

    private fun openFilterSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_filter)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val tvQty = bottomSheetDialog.findViewById<TextView>(R.id.tv_qty)
        val btnSubmit = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_filter)
        val sliderQty = bottomSheetDialog.findViewById<Slider>(R.id.slider_qty)

        val autoState =
            bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_state)
        val autoCity = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_city)
        val autoDistrict =
            bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_district)

        val modeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            Constants.stateArray
        )
        autoState?.setAdapter(modeAdapter)

        sliderQty?.addOnChangeListener { slider, value, fromUser ->
            tvQty?.text = value.toInt().toString()
        }
        btnSubmit?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun navigateToComingSoon(type: String) {
        /* Setting the animation for the navigation. */
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_to_right)
            .setPopEnterAnim(R.anim.slide_in_from_left).setPopExitAnim(R.anim.slide_out_left)

        val args = Bundle()
        args.putString("type", type)

        findNavController().navigate(R.id.comingSoonFragment, args, navBuilder.build())
    }

    private fun openImageDialog(imgLink: String) {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.alertdailog_image)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val imgView = dialog.findViewById<ImageViewZoom>(R.id.iv_pic)
        val btnClose = dialog.findViewById<MaterialButton>(R.id.btn_close)
        val tvUpiId = dialog.findViewById<TextView>(R.id.tv_upi_id)

        tvUpiId.text = preferenceManager.upiId
        Glide.with(requireContext()).load(preferenceManager.upiQrCode)
            .placeholder(R.drawable.ic_baseline_qr_code_2_24).into(imgView)

//        Glide.with(this)
//            .asBitmap()
//            .load(imgLink).placeholder(R.drawable.ic_baseline_qr_code_2_24).into(imgView)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.attributes = lp
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.filter_menu, menu)
        //map item
        menu.findItem(R.id.menu_filter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                openFilterSheet()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}