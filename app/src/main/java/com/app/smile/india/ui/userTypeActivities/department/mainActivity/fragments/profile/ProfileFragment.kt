/*
 * *
 *  * Created by Prady on 4/8/23, 11:38 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 11:34 AM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.app.smile.india.R
import com.app.smile.india.adapters.DashboardItemsAdapter
import com.app.smile.india.databinding.FragmentProfileBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.CommonUserIdReq
import com.app.smile.india.models.UserIdObj
import com.app.smile.india.models.dashCount.DashCountRes
import com.app.smile.india.models.userDetails.UserDetailsRes
import com.app.smile.india.ui.SplashActivity
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.calculateNoOfColumns
import com.app.smile.india.viewModels.SharedVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class ProfileFragment : Fragment(), KodeinAware, DashboardItemsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentProfileBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var slideFromRightAnimController: LayoutAnimationController
    private lateinit var fallDownAnimController: LayoutAnimationController

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
        setupAnimations()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        hitApis()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = true)
    }

    private fun hitApis() {
        getUserDetails(
            CommonUserIdReq(
                apiname = "GetUserDetail",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
        getDashboardItems(
            CommonUserIdReq(
                apiname = "getDashboardCounts",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
    }

    private fun getDashboardItems(dashCountReq: CommonUserIdReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.userIdRequestCommon = MutableLiveData()
        viewModel.userIdRequestCommon.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, DashCountRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.rvDashboard.apply {
                            val mNoOfColumns = context.calculateNoOfColumns(context, 160)
                            val gridManager = GridLayoutManager(
                                context,
                                mNoOfColumns,
                            )
                            layoutManager = gridManager
                            adapter = DashboardItemsAdapter(
                                context,
                                response.data,
                                this@ProfileFragment
                            )
                            layoutAnimation = fallDownAnimController
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.userIdRequestCommon(dashCountReq)
    }

    override fun onDashboardItemsClick() {
    }

    private fun getUserDetails(mobileNoReq: CommonUserIdReq) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.getUserDetails = MutableLiveData()
        viewModel.getUserDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, UserDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        preferenceManager.phone = response.data[0]?.Mobile
                        preferenceManager.userName = response.data[0]?.UserName
                        binding.apply {
                            tvName.text =
                                "${response.data[0]?.UserName}\n(${response.data[0]?.UserId})"
                            tvNumber.text = "+91-" + response.data[0]?.Mobile
                            ivProfileImg.load(Constants.imgBaseUrl + response.data[0]?.Photo) {
                                error(R.drawable.dummy_user)
                                placeholder(R.drawable.dummy_user)
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getUserDetails(mobileNoReq)
    }

    private fun setupListeners() {
        binding.apply {
            btnEdit.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                findNavController().navigate(action)
            }

            btnRefresh.setOnClickListener {
                getDashboardItems(
                    CommonUserIdReq(
                        apiname = "getDashboardCounts",
                        obj = UserIdObj(userId = "" + preferenceManager.userid)
                    )
                )
            }

//            cardLogout.setOnClickListener {
//                showLogoutDialog()
//            }

            btnBack.setOnClickListener {
                findNavController(it).popBackStack()
            }

//            cardSaveAddress.setOnClickListener {
//                val args = Bundle()
//                args.putString("title", "product")
//                findNavController().navigate(R.id.savedAddressFragment, args, getNavOptions())
//            }

            cardLeftRef.setOnClickListener {
                (activity as MainActivity).openWebIntent("https://smileindiaeshop.com/home/userregistration?userid=" + preferenceManager.userid + "&sp=1")
            }
            cardRightRef.setOnClickListener {
                (activity as MainActivity).openWebIntent("https://smileindiaeshop.com/home/userregistration?userid=" + preferenceManager.userid + "&sp=2")
            }
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

    private fun setupAnimations() {
        slideFromRightAnimController = AnimationUtils.loadLayoutAnimation(
            requireActivity(),
            R.anim.ll_slide_in_from_right
        )
        fallDownAnimController =
            AnimationUtils.loadLayoutAnimation(
                requireActivity(),
                R.anim.ll_fall_down
            )
    }


}