/*
 * *
 *  * Created by Prady on 4/8/23, 11:38 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 11:34 AM
 *
 */

package com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity.fragments.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.FragmentProfileBinding
import com.app.prady.appbase.factories.SharedVMF
import com.app.prady.appbase.helpers.Constants
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.models.UserIdRequest
import com.app.prady.appbase.models.userDetails.UserDetailsRes
import com.app.prady.appbase.ui.SplashActivity
import com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.prady.appbase.viewModels.SharedVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class ProfileFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentProfileBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        (activity as MainActivity?)?.setBottombarVisibility(false)
//        binding.tvAppVer.text = "App Version : " + BuildConfig.VERSION_NAME
//        hitApis()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity?)?.setBottombarVisibility(true)
    }

    private fun hitApis() {
//        getUserDetails(UserIdRequest("" + preferenceManager.userid))
    }

    private fun getUserDetails(mobileNoReq: UserIdRequest) {
        viewModel.getUserDetails = MutableLiveData()
        viewModel.getUserDetails.observe(requireActivity()) {
            val response = Gson().fromJson(it, UserDetailsRes::class.java)
//            Log.e("getUserDetails ", "$response")
            if (response != null) {
                if (response.status) {
                    binding.name.text = response.response.fullName + " (" + response.response.enrollmentNo + ")"
                    binding.number.text = "" + response.response.courseName
                } else {
                    (activity as MainActivity)?.apiErrorDialog(response.msg)
                }
            } else {
                (activity as MainActivity)?.apiErrorDialog(Constants.apiErrors)
            }
        }
        viewModel.getUserDetails(mobileNoReq)
    }

    private fun setupListeners() {
        binding.apply {
//            editProfile.setOnClickListener {
//                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
//                findNavController().navigate(action)
////                context?.toast("Sorry this section is currently locked.")
//            }

            cardLogout.setOnClickListener {
                showLogoutDialog()
            }

            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

//            cardOrderHist.setOnClickListener {
//                val args = Bundle()
//                args.putString("title", "product")
//                findNavController().navigate(R.id.orderHistoryFragment, args, getNavOptions())
//            }
//
//            cardSaveAddress.setOnClickListener {
//                val args = Bundle()
//                args.putString("title", "product")
//                findNavController().navigate(R.id.savedAddressFragment, args, getNavOptions())
//            }
        }
    }

    private fun showLogoutDialog() {
        CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout)
            .setTitle("LOGOUT OUT !!")
            .setMessage("Are you sure you want to log out from the app ?")
            .addButton(
                "Yes", -1, -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                preferenceManager.clear()
                startActivity(Intent(requireActivity(), SplashActivity::class.java))
                requireActivity().finishAffinity()
                dialog.dismiss()
            }
            .addButton(
                "No", -1, -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
}