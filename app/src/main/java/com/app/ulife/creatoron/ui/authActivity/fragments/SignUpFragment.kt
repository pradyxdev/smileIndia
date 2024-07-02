/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.ulife.creatoron.ui.authActivity.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.FragmentSignupBinding
import com.app.ulife.creatoron.factories.AuthVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.pincodeStateCity.GetPincodeStateCityReq
import com.app.ulife.creatoron.models.pincodeStateCity.GetPincodeStateCityRes
import com.app.ulife.creatoron.models.signUp.SignUpRes
import com.app.ulife.creatoron.models.signUp.tmp.SignUpNewReq
import com.app.ulife.creatoron.models.userDetails.UserDetailsReq
import com.app.ulife.creatoron.models.userDetails.UserDetailsRes
import com.app.ulife.creatoron.ui.authActivity.AuthActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.afterTextChanged
import com.app.ulife.creatoron.utils.hideKeyboard
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.viewModels.AuthVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.gson.Gson
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
    private var directionList = listOf("Left", "Right")
    private var directionIdList = listOf("1", "2")
    private var selectedDirection = ""

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
        binding.apply {
            try {
                if (!arguments?.getString("number").isNullOrEmpty()) {
                    etNumber.setText(arguments?.getString("number").toString())
                } else {
                    etNumber.setText("")
                }
            } catch (e: Exception) {
                Log.e("num ", "not found !")
            }
            val genderAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                Constants.genderArray
            )
            etGender.setAdapter(genderAdapter)

            val directionAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, directionList
            )
            actDirection.setAdapter(directionAdapter)
            actDirection.setOnItemClickListener { adapterView, view, i, l ->
                selectedDirection = directionIdList[i]
                println("selectedDirection : $selectedDirection")
            }
        }
    }

    private fun setupListeners() {
        binding.apply {

            btnBack.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToSigninFragment()
                findNavController().navigate(action)
            }

            btnVerifyMember.setOnClickListener {
                if (etSponsor.text.toString().isNotEmpty()) {
                    getUserDetails(
                        UserDetailsReq(userId = "" + etSponsor.text)
                    )
                } else {
                    etSponsor.error = "Enter Member ID first !"
                }
            }

            etSponsor.afterTextChanged {
                btnSignUp.isEnabled = false
                etSpUsername.setText("")
            }

            etPincode.afterTextChanged {
                if (!it.isNullOrEmpty()) {
                    if (it.length == 6) {
                        getPincodeStateCity(GetPincodeStateCityReq(Pincode = "" + etPincode.text))
                    } else {
                        etPincode.error = "Pincode should be 6 digit !"
                        etState.setText("")
                        etCity.setText("")
                        etArea.setText("")
                    }
                } else {
                    etPincode.error = "Pincode can't be empty !"
                    etState.setText("")
                    etCity.setText("")
                    etArea.setText("")
                }
            }

            btnSignUp.onDebouncedListener {
                when {
                    etSponsor.text.toString().isEmpty() -> etSponsor.error =
                        "Please enter your sponsor id"

                    etSpUsername.text.toString().isEmpty() -> etSponsor.error =
                        "Please enter your sponsor id"

                    etUsername.text.toString().isEmpty() -> etUsername.error =
                        "Please enter your name"

                    etEmail.text.toString().isEmpty() -> etEmail.error = "Please enter your email"
                    etGender.text.toString().isEmpty() -> etGender.error =
                        "Please select your gender"

                    etNumber.text.toString()
                        .isEmpty() || etNumber.text.toString().length < 9 -> etNumber.error =
                        "Please enter your phone number"
//                    etPass.text.toString().isEmpty() -> etPass.error = "Please enter your password"
//                    etPassConfirm.text.toString().isEmpty() -> etPassConfirm.error = "Please enter your password"
//                    !etPass.text.toString().equals(etPassConfirm.text.toString()) -> {
//                        etPass.error = "Password doesn't match !"
//                        etPassConfirm.error = "Password doesn't match !"
//                    }
                    else -> {
                        hitSignUpApi(
                            SignUpNewReq(
                                AdharNo = "" + etAadhaarNumber.text,
                                CountryId = 100,
                                PanNo = "" + etPanNumber.text,
                                Pincode = "" + etPincode.text,
                                VillageName = "" + etArea.text,
                                addresss = "" + etAddress.text,
                                cityname = "" + etCity.text,
                                email = "" + etEmail.text,
                                gender = "" + etGender.text,
                                mobile = "" + etNumber.text,
                                sponsorid = "" + etSponsor.text,
                                standingposition = "" + selectedDirection,
                                statename = "" + etState.text,
                                username = "" + etUsername.text
                            )

//                            SignUpReq(
//                                CountryId = "0",
//                                Password = "" + etPass.text,
//                                addresss = "",
//                                email = "" + etEmail.text,
//                                gender = "" + etGender.text,
//                                mobile = "" + etNumber.text,
//                                parentuserid = "",
//                                regtype = "free",
//                                sponsorid = "" + etSponsor.text,
//                                standingposition = "1",
//                                username = "" + etUsername.text
//                            )
                        )
                    }
                }
            }
        }
    }

    private fun hitSignUpApi(req: SignUpNewReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.signUp = MutableLiveData()
        viewModel.signUp.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, SignUpRes::class.java)
//            Log.e("signUpRes ", "$response")
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        when (response.data[0].id) {
                            1 -> {
                                showDialog(
                                    "" + response.data[0]?.userid,
                                    "" + response.data[0]?.password
                                )
                            }

                            else -> (activity as AuthActivity).apiErrorDialog("" + response.data[0]?.msg)
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as AuthActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as AuthActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.signUp(req)
    }

    private fun getUserDetails(mobileNoReq: UserDetailsReq) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.getUserDetails = MutableLiveData()
        viewModel.getUserDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, UserDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        if (!response.data.isNullOrEmpty()) {
                            binding.etSponsor.error = null
                            binding.btnSignUp.isEnabled = true
                            context?.hideKeyboard(binding.root)
//                            context?.toast("User found : ${response.data[0].UserName} (${mobileNoReq.obj.userId}")
                            binding.etSpUsername.setText("${response.data[0].UserName} (${mobileNoReq.userId})")
                        } else {
                            Log.e("getUserDetails ", "" + response)
                            binding.etSponsor.error = "User not found !"
                            binding.btnSignUp.isEnabled = false
                            binding.etSpUsername.setText("")
                        }
                    } else {
                        LoadingUtils.hideDialog()
//                        (activity as MainActivity).apiErrorDialog(response.message)
                        Log.e("getUserDetails ", "" + response.message)
                        binding.etSponsor.error = "User not found !"
                        binding.btnSignUp.isEnabled = false
                        binding.etSpUsername.setText("")
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
                    Log.e("getUserDetails ", "" + Constants.apiErrors)
                    binding.etSponsor.error = "User not found !"
                    binding.btnSignUp.isEnabled = false
                    binding.etSpUsername.setText("")
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as AuthActivity).apiErrorDialog("$it\n$e")
                binding.etSponsor.error = "User not found !"
                binding.btnSignUp.isEnabled = false
                binding.etSpUsername.setText("")
            }
        }
        viewModel.getUserDetails(mobileNoReq)
    }

    private fun getPincodeStateCity(req: GetPincodeStateCityReq) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.getPincodeStateCity = MutableLiveData()
        viewModel.getPincodeStateCity.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPincodeStateCityRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        context?.hideKeyboard(binding.root)

                        binding.apply {
                            etState.setText("" + response.data[0]?.StateName)
                            etCity.setText("" + response.data[0]?.DistrictName)
                            etArea.setText("" + response.data[0]?.AreaName)
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as AuthActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as AuthActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getPincodeStateCity(req)
    }

    private fun showDialog(userId: String, password: String) {
        CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_successfull)
            .setTitle("Account creation successful !!")
            .setMessage("Please save this user id & sign in using these credentials to continue.\nUSER ID : $userId \nPassword : $password")
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