/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.ulife.creator.ui.authActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.FragmentSigninBinding
import com.app.ulife.creator.factories.AuthVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.signin.SigninReq
import com.app.ulife.creator.models.signin.SigninRes
import com.app.ulife.creator.ui.authActivity.AuthActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.AuthVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SignInFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentSigninBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: AuthVMF by instance()
    private lateinit var viewModel: AuthVM
    private lateinit var preferenceManager: PreferenceManager
    private var sessionNumber = ""
    private val sessionNames: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
//        setupCustomMargins()

//        binding.apply {
//            etUserId.setText("SC000001")
//            etPass.setText("019144")
//        }
    }

    private fun setupListeners() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val action = SignInFragmentDirections.actionSigninFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            btnSignIn.setOnClickListener {
                when {
                    etUserId.text.toString().isEmpty() -> etUserId.error =
                        "Please enter your userid"
//                            || etNumber.text.toString().length < 10 -> etNumber.error =
//                        "Please enter your phone number"
                    etPass.text.toString().isEmpty() -> etPass.error = "Please enter your password"
                    else -> {
                        viewModel.setUserType(Constants.userTypeCustomer) //
                        btnSignIn.isEnabled = false
                        hitApi(
                            SigninReq(
                                ip = "1",
                                password = "" + etPass.text,
                                role = "User",
                                userid = "" + etUserId.text
                            )
                        )
                    }
                }
            }
        }
    }

    private fun hitApi(loginReq: SigninReq) {
        LoadingUtils.showDialog(requireContext(), false)
        viewModel.signIn = MutableLiveData()
        viewModel.signIn.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, SigninRes::class.java)
//            Log.e("response", "$response")
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.btnSignIn.isEnabled = false
                        preferenceManager.token = "" + response.token

                        when (response.data[0].id) {
                            1 -> {
                                preferenceManager.userid = "" + response.data[0].UserId
                                openNextUI()
                            }

                            else -> (activity as AuthActivity).apiErrorDialog("Sorry, your credentials were invalid !")
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as AuthActivity).apiErrorDialog("Sorry, your credentials were invalid !")
//                    binding.progressBar.visibility = View.GONE
                        binding.btnSignIn.isEnabled = true
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.btnSignIn.isEnabled = true
                    (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.btnSignIn.isEnabled = true
                (activity as AuthActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.signIn(loginReq)
    }

    private fun openNextUI() {
        when (viewModel.userType.value) {
            Constants.userTypeCustomer -> {
                preferenceManager.userType = Constants.userTypeCustomer
                activity?.startActivity(Intent(activity, MainActivity::class.java))
                activity?.overridePendingTransition(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_left
                )
                activity?.finishAffinity()
            }
//            Constants.userTypeDepartment -> {
//                preferenceManager.userType = Constants.userTypeAdmin
//                activity?.startActivity(Intent(activity, VendorMainActivity::class.java))
//                activity?.overridePendingTransition(
//                    R.anim.slide_in_from_right,
//                    R.anim.slide_out_left
//                )
//                activity?.finishAffinity()
//            }
            else -> context?.toast("Unable to get user type !")
        }
    }

    private fun setupCustomMargins() {
        // status bar height
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        // navigation bar height
        var navigationBarHeight = 0
        val resourceId2 = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId2)
        }

        val param = binding.llContainer.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, statusBarHeight, 0, navigationBarHeight)
        binding.llContainer.layoutParams = param
    }
}