/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.reports.passwordManage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentPasswordContainerBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.EmptyResponse
import com.app.smile.india.models.UserIdRequest
import com.app.smile.india.ui.SplashActivity
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.utils.AlertDialogUtil.apiAlertDialog
import com.app.smile.india.utils.AlertDialogUtil.showAlertDialog
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PasswordContainerFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentPasswordContainerBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
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

            card1.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.changePasswordFragment, args, getNavOptions())
            }

            card2.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(
                    R.id.changeTransPasswordFragment,
                    args,
                    getNavOptions()
                )
            }

            card3.setOnClickListener {
                context?.showAlertDialog(
                    title = "Password Recovery !",
                    message = "Are you sure you want to start your password recovery ?",
                    posBtnText = "Yes",
                    negBtnText = "No",
                    showNegBtn = true,
                    callback = {
                        passwordRecovery(UserIdRequest(userid = "" + preferenceManager.userid))
                    })
            }
        }
    }

    private fun passwordRecovery(request: UserIdRequest) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.passwordRecovery = MutableLiveData()
        viewModel.passwordRecovery.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, EmptyResponse::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        context?.apiAlertDialog(
                            isError = false,
                            title = "Password Recovery Successful !",
                            subTitle = "Password has been sent to your registered email !",
                            action = {
                                context?.toast("Signing out...")
                                context?.toast("Login In again using password you received on your email !")

                                preferenceManager.clear()
                                activity?.startActivity(
                                    Intent(
                                        requireActivity(),
                                        SplashActivity::class.java
                                    )
                                )
                                requireActivity().finishAffinity()
                            })
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
        viewModel.passwordRecovery(request)
    }
}