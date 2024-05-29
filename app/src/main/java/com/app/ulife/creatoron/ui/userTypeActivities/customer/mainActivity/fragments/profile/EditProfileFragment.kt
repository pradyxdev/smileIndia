/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.profile

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.load
import com.app.ulife.creatoron.databinding.FragmentEditProfileBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.CommonUserIdReq
import com.app.ulife.creatoron.models.UserIdObj
import com.app.ulife.creatoron.models.passwordManage.changePassword.ChangePasswordRes
import com.app.ulife.creatoron.models.updateProfile.Obj
import com.app.ulife.creatoron.models.updateProfile.UpdateProfileReq
import com.app.ulife.creatoron.models.userDetails.UserDetailsRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.toast
import com.app.ulife.creatoron.viewModels.SharedVM
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEditProfileBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var imgUri = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        getUserDetails(
            CommonUserIdReq(
                apiname = "GetUserDetail",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
    }

    private fun setupListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }

            ivProfileImg.setOnClickListener {
                ImagePicker.with(requireActivity())
                    .crop()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            btnSubmit.setOnClickListener {
                updateUserDetails(
                    UpdateProfileReq(
                        apiname = "UpdateProfile", obj = Obj(
                            Aadhar = "" + etAadhaarNumber.text,
                            AccHolderName = "" + etBankHolderName.text,
                            AccNo = "" + etBankAcNum.text,
                            BankName = "" + etBankName.text,
                            BranchName = "" + etBankBranch.text,
                            DateOfBirth = "${etMonth.text}-${etDay.text}-${etYear.text}",
                            Gst_No = "" + etGstName.text,
                            IFSC = "" + etBankIfsc.text,
                            Nominee_Name = "" + etNomineName.text,
                            Nominee_Relation = "",
                            PanNo = "" + etPanNo.text,
                            Pincode = "" + etPincode.text,
                            Profession = "" + etProfession.text,
                            Voter_No = "" + etVoterName.text,
                            address = "" + etAddress.text,
                            email = "" + etEmail.text,
                            gender = "" + etGender.text,
                            guardian = "" + etParentsName.text,
                            phone = "" + etNumber.text,
                            photo = "" + imgUri,
                            userid = "" + preferenceManager.userid,
                            username = "" + etUsername.text
                        )
                    )
                )
            }
        }
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
                        binding.apply {
                            ivProfileImg.load(Constants.imgBaseUrl + response.data[0]?.Photo) {
                                error(null)
                                placeholder(null)
                            }

                            etUsername.setText("" + response.data[0]?.UserName)
                            etEmail.setText("" + response.data[0]?.Email)
                            etNumber.setText("+91-" + response.data[0]?.Mobile)
                            etParentsName.setText("" + response.data[0]?.ParentName)
                            etGender.setText("" + response.data[0]?.Gender)
                            etAddress.setText("" + response.data[0]?.Address)
                            etPincode.setText("" + response.data[0]?.Pincode)
                            etProfession.setText("" + response.data[0]?.Profession)
                            etNomineName.setText("" + response.data[0]?.Nominee_Name)

                            if (response.data[0]?.KycStatus.equals("Approved")) {
                                etlBankName.isEnabled = false
                                etlBankBranch.isEnabled = false
                                etlBankHolderName.isEnabled = false
                                etlBankAcNum.isEnabled = false
                                etlBankIfsc.isEnabled = false

                                etlPanNo.isEnabled = false
                                etlAadhaarNumber.isEnabled = false
                                etlGstName.isEnabled = false
                                etlVoterName.isEnabled = false
                            }

                            etBankName.setText("" + response.data[0]?.BankName)
                            etBankBranch.setText("" + response.data[0]?.BranchName)
                            etBankHolderName.setText("" + response.data[0]?.AccHolderName)
                            etBankAcNum.setText("" + response.data[0]?.AccNo)
                            etBankIfsc.setText("" + response.data[0]?.IFSC)

                            if (!response.data[0]?.DateOfBirth.toString().isNullOrEmpty()) {
                                if (response.data[0]?.DateOfBirth.toString().contains("-")) {
                                    val dob = response.data[0]?.DateOfBirth.toString().split("-")
                                    etDay.setText("" + dob[2])
                                    etMonth.setText("" + dob[1])
                                    etYear.setText("" + dob[0])
                                }
                            }

                            etPanNo.setText("" + response.data[0]?.PanNo)
                            etAadhaarNumber.setText("" + response.data[0]?.Aadhar)
                            etGstName.setText("" + response.data[0]?.Gst_No)
                            etVoterName.setText("" + response.data[0]?.Voter_No)

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                Constants.genderArray
                            )
                            binding.etGender.setAdapter(adapter)
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
        viewModel.getUserDetails(mobileNoReq)
    }

    private fun updateUserDetails(mobileNoReq: UpdateProfileReq) {
        LoadingUtils.showDialog(requireActivity(), false)
        viewModel.updateUserDetails = MutableLiveData()
        viewModel.updateUserDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, ChangePasswordRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        when (response.data[0].id) {
                            1 -> (activity as MainActivity).apiSuccessDialog("Profile updated successfully !") {
                                getUserDetails(
                                    CommonUserIdReq(
                                        apiname = "GetUserDetail",
                                        obj = UserIdObj(userId = "" + preferenceManager.userid)
                                    )
                                )
                            }

                            else -> (activity as MainActivity).apiErrorDialog(response.data[0].msg)
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
        viewModel.updateUserDetails(mobileNoReq)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            Log.e("onActivityResult ", "$resultCode // $data")
            try {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //Image Uri will not be null for RESULT_OK
                        val fileUri = data?.data!!
                        Log.e("fileUri ", "" + fileUri)
                        binding.ivProfileImg.setImageURI(fileUri)
                        convertToBase64(fileUri)
                    }

                    ImagePicker.RESULT_ERROR -> {
                        context?.toast(ImagePicker.getError(data))
                    }

                    else -> {
                        context?.toast("Image upload failed, please try again !")
                    }
                }
            } catch (e: Exception) {
                context?.toast("Image upload failed, please try again !")
            }
        }

    private fun convertToBase64(fileUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(
            context?.contentResolver,
            fileUri
        ) // initialize byte stream
        val stream = ByteArrayOutputStream() // compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream) // Initialize byte array
        val bytes = stream.toByteArray()
        imgUri = Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}