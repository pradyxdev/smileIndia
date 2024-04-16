/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.reports.epinManage.tabs

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.load
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.FragmentEpinRequestBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.cpyDetails.GetCpyDetailsRes
import com.app.ulife.creator.models.epinManage.epinRequest.EpinRequestReq
import com.app.ulife.creator.models.epinManage.epinTopup.EpinTopupRes
import com.app.ulife.creator.models.epinManage.planList.GetPlanRes
import com.app.ulife.creator.models.wallet.Obj
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.afterTextChanged
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import java.io.ByteArrayOutputStream

class EpinRequestFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEpinRequestBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var pkgId = ""
    private var epinTypeId = ""
    private var walletAmount = 0.0
    private var imgUri = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpinRequestBinding.inflate(layoutInflater)
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
            tvToolbarTitle.text = "E-PIN Request"

            getCompanyDetails(
                EmptyRequest(
                    apiname = "GetChainDetail",
                    obj = com.app.ulife.creator.models.Obj()
                )
            )

            getPlanList(
                EmptyRequest(
                    apiname = "GetPlan", obj = com.app.ulife.creator.models.Obj(),
                )
            )

            tvWalletAmt.visibility = View.VISIBLE
            getWalletBalance(
                WalletReq(
                    apiname = "GetWalletBalance", obj = Obj(
                        datatype = "T",
                        transactiontype = "",
                        userid = "" + preferenceManager.userid,
                        wallettype = "F"
                    )
                )
            )
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

            ivProductImageRequest.setOnClickListener {
                ImagePicker.with(requireActivity())
                    .crop()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            etNumEpin.afterTextChanged {
                try {
                    val pkgAmt = etPkgAmt.text.toString().toDouble()
                    val myNumGen = etNumEpin.text.toString().toDouble()
                    val totAmt = pkgAmt * myNumGen
                    etTotAmt.setText("$totAmt")

                    if (totAmt <= walletAmount) {
                        btnSubmit.isEnabled = true
                        etTotAmt.error = null
                    } else {
                        btnSubmit.isEnabled = false
                        etTotAmt.error = "Insufficient funds !"
                        context?.toast("Insufficient funds !")
                    }
                } catch (e: Exception) {
                    Log.e("etNumEpin ", "$e")
                    etTotAmt.setText("")
                    btnSubmit.isEnabled = false
                }
            }

//            btnVerifyMember.setOnClickListener {
//                if (etToUserid.text.toString().isNotEmpty()) {
//                    getUserDetails(
//                        CommonUserIdReq(
//                            apiname = "GetUserDetail",
//                            obj = UserIdObj(userId = "" + etToUserid.text)
//                        )
//                    )
//                } else {
//                    etToUserid.error = "Enter Member ID first !"
//                }
//            }
//
//            etToUserid.afterTextChanged {
//                btnSubmit.isEnabled = false
//                etUsername.setText("")
//            }

            btnSubmit.onDebouncedListener {
                when {
//                    etToUserid.text.toString().isEmpty() -> etToUserid.error = "Please enter user id"

                    actPkg.text.toString().isEmpty() -> actPkg.error = "Please select your package"
                    etNumEpin.text.toString().isEmpty() -> etNumEpin.error =
                        "Please enter num of e-pin"

                    else -> epinGenerate(
//                        obj = com.app.ulife.creator.models.epinManage.epinRequest.Obj(
//                            entryby = "" + preferenceManager.userid,
//                            noofpin = "" + etNumEpin.text,
//                            planid = "" + pkgId,
//                            userid = "" + preferenceManager.userid
//                        )
                        EpinRequestReq(
                            apiname = "InsertEPinRequest",
                            obj = com.app.ulife.creator.models.epinManage.epinRequest.Obj(
                                userid = "" + preferenceManager.userid,
                                packageid = "$pkgId",
                                amount = "" + etPkgAmt.text,
                                noofepin = "" + etNumEpin.text,
                                totalamount = "" + etTotAmt.text,
                                transactionno = "" + etTransId.text,
                                remark = "" + etRemark.text,
                                reciept = "" + imgUri,
                                RequestFrom = "App"
                            ),
                        )
                    )
                }
            }
        }
    }

    private fun epinGenerate(req: EpinRequestReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.epinRequest = MutableLiveData()
        viewModel.epinRequest.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, EpinTopupRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        if (!response.data.isNullOrEmpty()) {
                            when (response.data[0].id) {
                                1 -> (activity as MainActivity).apiSuccessDialog(successMsg = "${response.data[0].msg}",
                                    action = {
                                        Navigation.findNavController(binding.root).popBackStack()
                                    })

                                else -> (activity as MainActivity).apiErrorDialog("${response.data[0].msg}")
                            }
                        } else {
                            (activity as MainActivity).apiErrorDialog(it)
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
        viewModel.epinRequest(req)
    }

//    private fun getUserDetails(mobileNoReq: CommonUserIdReq) {
//        LoadingUtils.showDialog(requireActivity(), false)
//        viewModel.getUserDetails = MutableLiveData()
//        viewModel.getUserDetails.observe(requireActivity()) {
//            try {
//                val response = Gson().fromJson(it, UserDetailsRes::class.java)
//                if (response != null) {
//                    if (response.status) {
//                        LoadingUtils.hideDialog()
//                        if (!response.data.isNullOrEmpty()) {
//                            binding.etToUserid.error = null
//                            binding.btnSubmit.isEnabled = true
//                            context?.hideKeyboard(binding.root)
////                            context?.toast("User found : ${response.data[0].UserName} (${mobileNoReq.obj.userId}")
//                            binding.etUsername.setText("${response.data[0].UserName} (${mobileNoReq.obj.userId})")
//                        } else {
//                            Log.e("getUserDetails ", "" + response)
//                            binding.etToUserid.error = "User not found !"
//                            binding.btnSubmit.isEnabled = false
//                            binding.etUsername.setText("")
//                        }
//                    } else {
//                        LoadingUtils.hideDialog()
////                        (activity as MainActivity).apiErrorDialog(response.message)
//                        Log.e("getUserDetails ", "" + response.message)
//                        binding.etToUserid.error = "User not found !"
//                        binding.btnSubmit.isEnabled = false
//                        binding.etUsername.setText("")
//                    }
//                } else {
//                    LoadingUtils.hideDialog()
//                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
//                    Log.e("getUserDetails ", "" + Constants.apiErrors)
//                    binding.etToUserid.error = "User not found !"
//                    binding.btnSubmit.isEnabled = false
//                    binding.etUsername.setText("")
//                }
//            } catch (e: Exception) {
//                (activity as MainActivity).apiErrorDialog("$it\n$e")
//                binding.etToUserid.error = "User not found !"
//                binding.btnSubmit.isEnabled = false
//            }
//        }
//        viewModel.getUserDetails(mobileNoReq)
//    }

    private fun getPlanList(req: EmptyRequest) {
        viewModel.emptyRequestCommon = MutableLiveData()
        viewModel.emptyRequestCommon.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPlanRes::class.java)
                if (response != null) {
                    if (response.status) {
                        val listName = mutableListOf<String>()
                        listName.clear()
                        val listId = mutableListOf<String>()
                        listId.clear()
                        val listAmt = mutableListOf<String>()
                        listId.clear()

                        for (i in response.data.indices) {
                            listName.add("${response.data[i]?.PlanName}, (${response.data[i]?.Amount})")
                            listId.add("" + response.data[i]?.Id)
                            listAmt.add("" + response.data[i]?.Amount)
                        }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            listName
                        )
                        binding.actPkg.setAdapter(adapter)
                        binding.actPkg.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                pkgId = listId[position]
                                binding.etPkgAmt.setText("${listAmt[position]}")
                            }
                    } else {
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.emptyRequestCommon(req)
    }

    private fun getWalletBalance(req: WalletReq) {
        viewModel.getWalletBalance = MutableLiveData()
        viewModel.getWalletBalance.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetWalletBalRes::class.java)
                if (response != null) {
                    if (response.status) {
                        walletAmount = response.data[0]?.Balance.toString().toDouble()
                        binding.apply {
                            tvWalletAmt.text = "" + response.data[0]?.Balance
                        }
                    } else {
                        binding.apply {
                            walletAmount = 0.0
                            tvWalletAmt.text = "0.0"
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        walletAmount = 0.0
                        tvWalletAmt.text = "0.0"
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                walletAmount = 0.0
                binding.apply {
                    tvWalletAmt.text = "0.0"
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }

    private fun getCompanyDetails(emptyRequest: EmptyRequest) {
        LoadingUtils.showDialog(context, false)
        viewModel.getCompanyDetails = MutableLiveData()
        viewModel.getCompanyDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCpyDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            tvCpyDetails.text = Html.fromHtml("" + response.data[0]?.PublicKey)
                            ivCompanyQr.load("" + response.data[0]?.QRImage) {
                                error(R.drawable.logo)
                                placeholder(R.drawable.logo)
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
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getCompanyDetails(emptyRequest)
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
                        binding.ivProductImageRequest.setImageURI(fileUri)
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
//        binding.btnSubmitAddRequest.isEnabled = true
//        binding.ivCrossAddReq.visibility = View.VISIBLE
    }
}