/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.passwordManage.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creatoron.databinding.FragmentChangePasswordBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.passwordManage.changePassword.ChangePasswordReq
import com.app.ulife.creatoron.models.passwordManage.changePassword.ChangePasswordRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PasswordRecoveryFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentChangePasswordBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding.apply {
            tvToolbarTitle.text = "Change Transaction Password"
            tvWalletAmt.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

            btnSubmit.onDebouncedListener {
                when {
                    etOldPassword.text.toString().isEmpty() -> etOldPassword.error =
                        "Please enter your password"

                    etNewPassword.text.toString().isEmpty() -> etNewPassword.error =
                        "Please enter your password"

                    etConfirmPassword.text.toString().isEmpty() -> etConfirmPassword.error =
                        "Please enter your password"

                    !etNewPassword.text.toString().equals(etConfirmPassword.text.toString()) -> {
                        etNewPassword.error = "Password doesn't match !"
                        etConfirmPassword.error = "Password doesn't match !"
                    }

                    else -> changePassword(
                        ChangePasswordReq(
                            apiname = "ChangeTranPassword",
                            obj = com.app.ulife.creatoron.models.passwordManage.changePassword.Obj(
                                newpassword = "" + etNewPassword.text,
                                oldpassword = "" + etOldPassword.text,
                                userid = "" + preferenceManager.userid
                            )
                        )
                    )
                }
            }
        }
    }

    private fun changePassword(mobileNoReq: ChangePasswordReq) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.changeTransPassword = MutableLiveData()
        viewModel.changeTransPassword.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, ChangePasswordRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        if (!response.data.isNullOrEmpty()) {
                            when (response.data[0].id) {
                                1 -> (activity as MainActivity).apiSuccessDialog("Password changed successfully !") {
                                    Navigation.findNavController(binding.root).popBackStack()
                                }

                                else -> (activity as MainActivity).apiErrorDialog(response.data[0].msg)
                            }
                        } else {
                            (activity as MainActivity).apiErrorDialog("" + response)
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
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.changeTransPassword(mobileNoReq)
    }
}