/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.smile.india.ui.authActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentSigninBinding
import com.app.smile.india.factories.AuthVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.signin.SigninReq
import com.app.smile.india.models.signin.SigninRes
import com.app.smile.india.ui.authActivity.AuthActivity
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.utils.setErrorWithFocus
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.AuthVM
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
    private var userTypeList = listOf("User", "Franchise")
    private var selectedUserType = ""

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

        binding.apply {
//            etUserId.setText("SI12345")
//            etPass.setText("a")

            val userTypeAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, userTypeList
            )
            actUsertype.setAdapter(userTypeAdapter)
            actUsertype.setOnItemClickListener { adapterView, view, i, l ->
                selectedUserType = userTypeList[i]
                println("selectedUserType : $selectedUserType")
            }
        }
//        binding.apply {
//            etUserId.setText("SC002265")
//            etPass.setText("1234")
//        }
    }

    private fun setupListeners() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val action = SignInFragmentDirections.actionSigninFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            btnForgot.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.forgotFragment, args, getNavOptions())
            }
            btnSignIn.setOnClickListener {
                when {
//                    selectedUserType.isEmpty() ->  actUsertype.setErrorWithFocus("Please select your type")
                    etUserId.text.toString().isEmpty() -> etUserId.error =
                        "Please enter your number"
//                            || etNumber.text.toString().length < 10 -> etNumber.error =
//                        "Please enter your phone number"
//                    etPass.text.toString().isEmpty() -> etPass.error = "Please enter your password"
                    else -> {
                        viewModel.setUserType(Constants.userTypeCustomer)
                        btnSignIn.isEnabled = false
                        hitApi(
                            SigninReq(
                                ip = "1",
                                password = "" + etUserId.text,
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
                (activity as AuthActivity).apiErrorDialog("$e\n$it")
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