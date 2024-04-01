/*
 * *
 *  * Created by Prady on 4/8/23, 11:38 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 11:24 AM
 *
 */

package com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity.fragments.home

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.FragmentHomeBinding
import com.app.prady.appbase.factories.SharedVMF
import com.app.prady.appbase.helpers.Constants
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.ui.SplashActivity
import com.app.prady.appbase.viewModels.SharedVM
import com.bumptech.glide.Glide
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.Slider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentHomeBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { // in here you can do logic when backPress is clicked
                showExitDialog()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
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
//        setHasOptionsMenu(true)
        preferenceManager.loggedIn = "true"
        Log.e("sessionDetails ", "" + preferenceManager.session)
//        showViewStub()
//        setupRandomSearchTxt()

//        openNewUserDialog()
//        (activity as MainActivity?)?.setToolbarIcon(R.drawable.box_location)
//        (activity as MainActivity?)?.setToolbarTitle(GPSTracker(context).getLocality(context))
    }

    private fun setupListeners() {
        binding.apply {
            btnLogout.setOnClickListener {
                showLogoutDialog()
            }
            btnProfile.setOnClickListener {
//                context?.toast("Coming soon !")
                val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun hitApis() {

    }

    private fun openFilterSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_filter)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val tvQty = bottomSheetDialog.findViewById<TextView>(R.id.tv_qty)
        val btnSubmit = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_filter)
        val btnClose = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_close)
        val sliderQty = bottomSheetDialog.findViewById<Slider>(R.id.slider_qty)

        val autoState =
            bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_state)
        val autoCity = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_city)
        val autoDistrict =
            bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.auto_complete_district)

        val modeAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, Constants.stateArray
        )
        autoState?.setAdapter(modeAdapter)

        sliderQty?.addOnChangeListener { slider, value, fromUser ->
            tvQty?.text = value.toInt().toString()
        }
        btnSubmit?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        btnClose?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun navigateToComingSoon(type: String) {
        /* Setting the animation for the navigation. */
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.slide_in_from_right).setExitAnim(R.anim.slide_out_to_right)
            .setPopEnterAnim(R.anim.slide_in_from_left).setPopExitAnim(R.anim.slide_out_left)

        val args = Bundle()
        args.putString("type", type)

        findNavController().navigate(R.id.comingSoonFragment, args, navBuilder.build())
    }

    private fun showExitDialog() {
        CFAlertDialog.Builder(context).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout).setTitle("Exit App !!")
            .setMessage("Are you sure you want to exit from this app ?").addButton(
                "Yes",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                requireActivity().finishAffinity()
                dialog.dismiss()
            }.addButton(
                "No",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }.setCancelable(true).show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val profileMenuItem = menu.findItem(R.id.menu_profile)
        val rootView = profileMenuItem.actionView as FrameLayout?
        val profileImg = rootView!!.findViewById<View>(R.id.iv_profile_img) as ImageView
        Glide.with(requireContext()).load(preferenceManager.profileImgUrl)
            .placeholder(R.drawable.dummy_user).into(profileImg)

        rootView.setOnClickListener {
            onOptionsItemSelected(profileMenuItem)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear() // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile -> {
                val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                findNavController().navigate(action)
            }
            R.id.menu_logout -> {
                showLogoutDialog()
            }
            R.id.menu_exit -> {
                showExitDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        CFAlertDialog.Builder(context).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout).setTitle("LOGOUT OUT !!")
            .setMessage("Are you sure you want to log out from the app ?").addButton(
                "Yes",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                preferenceManager.clear()
                startActivity(Intent(requireActivity(), SplashActivity::class.java))
                requireActivity().finishAffinity()
                dialog.dismiss()
            }.addButton(
                "No",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }.setCancelable(true).show()
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

    private fun datePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
//                .setValidator(DateValidatorPointForward.now())
        builder.setCalendarConstraints(constraintsBuilder.build())
        val now = Calendar.getInstance()
//            builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

        val picker = builder.build()
        picker.show(activity?.supportFragmentManager!!, picker.toString())
        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            val startDateStr = SimpleDateFormat("dd/MM/yyyy").format(it)
//            binding.tvDob.text = "DOB ($startDateStr)"
        }
    }

    private fun openNewUserDialog() {
        val dialog = Dialog(requireActivity())
//        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_header_new_user)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val button = dialog.findViewById<MaterialButton>(R.id.btn_search)
        button.setOnClickListener {
            dialog.dismiss()
//            startActivity(Intent(activity, ProfileCreationActivity::class.java))
//            activity?.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
        }
        dialog.show()
        dialog.window?.attributes = lp
    }

//    private fun showViewStub() {
//        if (binding.viewStub == null) {
//        }
//        binding.viewStub.visibility = View.VISIBLE
//    }
//
//    private fun hideViewStub() {
//        if (binding.viewStub == null) {
//            return
//        }
//        binding.viewStub.visibility = View.GONE
//    }
}