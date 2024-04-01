/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.prady.appbase.ui.authActivity.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.FragmentSignupBinding
import com.app.prady.appbase.factories.AuthVMF
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.viewModels.AuthVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.sports.battle.utils.onDebouncedListener
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SignUpFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentSignupBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: AuthVMF by instance()
    private lateinit var viewModel: AuthVM

    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthVM::class.java]
//        setupCustomMargins()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        try {
            if (!arguments?.getString("number").isNullOrEmpty()) {
                binding.etNumber.setText(arguments?.getString("number").toString())
            } else {
                binding.etNumber.setText("")
            }
        } catch (e: Exception) {
            Log.e("num ", "not found !")
        }
    }

    private fun setupListeners() {
        binding.apply {

            btnBack.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToSigninFragment()
                findNavController().navigate(action)
            }

            btnSignUp.onDebouncedListener {
                when {
                    etUsername.text.toString().isEmpty() -> etUsername.error = "Please enter your name"
                    etNumber.text.toString()
                        .isEmpty() || etNumber.text.toString().length < 9 -> etNumber.error =
                        "Please enter your phone number"
//                    etPass.text.toString().isEmpty() -> etPass.error = "Please enter your password"
//                    etPass.text.toString().isEmpty() -> etPass.error = "Please enter your password"
                    else -> {
//                        hitSignUpApi(
//                            SignupReq(
//                                "" + etEmail.text,
//                                "" + etNumber.text,
//                                "" + etName.text,
//                                ""
//                            )
//                        )
                        showDialog()
                    }
                }
            }
        }
    }

//    private fun hitSignUpApi(req: SignupReq) {
//        LoadingUtils.showDialog(context, false)
//        viewModel.signUp = MutableLiveData()
//        viewModel.signUp.observe(requireActivity()) {
//            val response = Gson().fromJson(it, EmptyResponse::class.java)
//            Log.e("signUpRes ", "$response")
//            if (response != null) {
//                if (response.status) {
//                    LoadingUtils.hideDialog()
//                    showDialog()
//                } else {
//                    LoadingUtils.hideDialog()
//                    binding.root.snackbar(response.message, "i")
//                    (activity as AuthActivity).apiErrorDialog(response.message)
//                }
//            } else {
//                LoadingUtils.hideDialog()
//                (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
//            }
//        }
//        viewModel.signUp(req)
//    }

    private fun showDialog() {
        CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_successfull)
            .setTitle("Account creation successful !!")
            .setMessage("Please sign in using these credential to continue.")
            .addButton(
                "SIGN IN", -1, -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                val action = SignUpFragmentDirections.actionSignUpFragmentToSigninFragment()
                findNavController().navigate(action)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
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