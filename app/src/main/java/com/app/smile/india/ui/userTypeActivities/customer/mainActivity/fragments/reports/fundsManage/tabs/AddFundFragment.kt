/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.reports.fundsManage.tabs

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
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.load
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentAddFundBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.EmptyRequest
import com.app.smile.india.models.cpyDetails.GetCpyDetailsRes
import com.app.smile.india.models.fundsManage.add.AddFundsReq
import com.app.smile.india.models.fundsManage.add.AddFundsRes
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.onDebouncedListener
import com.app.smile.india.utils.snackbar
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import java.io.ByteArrayOutputStream

class AddFundFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentAddFundBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var imgUri = ""
    private var walletTypeId = ""

    //    private val walletTypeArray = arrayListOf("Fund Wallet", "Recharge Wallet")
    private val walletTypeArray = arrayListOf("Recharge Wallet")

    //    private val walletTypeIdArray = arrayListOf("F", "RC")
    private val walletTypeIdArray = arrayListOf("RC")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFundBinding.inflate(layoutInflater)
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
            tvToolbarTitle.text = "Add Funds"
            tvWalletAmt.visibility = View.GONE

            val walletAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                walletTypeArray
            )
            actWallet.setAdapter(walletAdapter)
            actWallet.setOnItemClickListener { adapterView, view, i, l ->
                walletTypeId = walletTypeIdArray[i]
            }
        }
        getCompanyDetails(
            EmptyRequest(
                apiname = "GetChainDetail",
                obj = com.app.smile.india.models.Obj()
            )
        )
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
                    //.crop(3f, 2f) //Crop image(Optional), Check Customization for more option
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            btnSubmit.onDebouncedListener {
                when {
                    actWallet.text.toString().isEmpty() -> actWallet.error =
                        "Please enter your wallet type"

                    etAmount.text.toString().isEmpty() -> etAmount.error =
                        "Please enter your amount"

                    etTransId.text.toString().isEmpty() -> etTransId.error =
                        "Please enter your transaction id"

                    etRemark.text.toString().isEmpty() -> etRemark.error =
                        "Please enter your remark"

                    imgUri.isEmpty() -> root.snackbar("Please upload receipt !")
                    else -> addFunds(
                        AddFundsReq(
                            apiname = "InsertDepositRequest",
                            obj = com.app.smile.india.models.fundsManage.add.Obj(
                                ReqwalleyType = "" + walletTypeId,
                                Type = "User",
                                amount = "" + etAmount.text,
                                chainid = "0x38",
                                coinprice = "0",
                                dollerprice = "0",
                                fromaccount = "0",
                                reciept = "$imgUri",
                                remark = "" + etRemark.text,
                                reqtype = "Admin",
                                toaccount = "0",
                                transactionid = "" + etTransId.text,
                                transfercoin = "0",
                                userid = "" + preferenceManager.userid,
                                RequestFrom = "App"
                            )
                        )
                    )
                }
            }
        }
    }

    private fun addFunds(req: AddFundsReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.addFunds = MutableLiveData()
        viewModel.addFunds.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, AddFundsRes::class.java)
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.addFunds(req)
    }

    private fun getCompanyDetails(emptyRequest: EmptyRequest) {
        LoadingUtils.showDialog(context, false)
        viewModel.emptyRequestCommon = MutableLiveData()
        viewModel.emptyRequestCommon.observe(requireActivity()) {
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.emptyRequestCommon(emptyRequest)
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