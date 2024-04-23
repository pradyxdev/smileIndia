/*
 * *
 *  * Created by Prady on 4/20/24, 2:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 10:26 AM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.fastag

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.FragmentFastagRechargeBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.Coroutines
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.EmptyResponse
import com.app.ulife.creator.models.UserIdRequest
import com.app.ulife.creator.models.paysprint.fastag.psFastagOp.GetPsFastagOpListRes
import com.app.ulife.creator.models.paysprint.fastag.psFastagRecharge.DoPsFastagRechargeReq
import com.app.ulife.creator.models.paysprint.fastag.psFetchFastag.GetPsFetchFastagReq
import com.app.ulife.creator.models.paysprint.fastag.psFetchFastag.GetPsFetchFastagRes
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.GPSTracker
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.afterTextChanged
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.utils.snackbar
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class FastagRechargeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentFastagRechargeBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var rechargeTypeName = ""
    private var rechargeType = ""
    private var walletAmt = 0.0

    private var nameArray = ArrayList<String>()
    private var idArray = ArrayList<String>()
    private var operator = 0
    private var latitude = ""
    private var longitude = ""
    private var opId = ""
    var reffNo = ""
    private var canContinue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFastagRechargeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        rechargeTypeName = "" + arguments?.getString("rechargeTypeName")
        rechargeType = "" + arguments?.getString("rechargeType")
        binding.tvToolbarTitle.text = rechargeTypeName

        Coroutines.main {
            delay(1500)
            setupLocation()
        }

        if (!rechargeType.isNullOrEmpty()) {
            Log.e("rechargeType ", "--$rechargeType--")

            binding.apply {
                etAmount.afterTextChanged {
                    try {
                        if (it.toInt() > 10001) {
                            binding.etAmount.error = "Value can't be greater than Rs 10,000 !"
                            canContinue = false
                        } else if (it.toInt() < 100) {
                            binding.etAmount.error = "Value can't be less than Rs 100 !"
                            canContinue = false
                        } else if (it.toInt() == 0 || it.isNullOrEmpty()) {
                            binding.etAmount.error = "Value can't be empty !"
                            canContinue = false
                        } else {
                            binding.etAmount.error = null
                            canContinue = true
                        }
                    } catch (e: Exception) {
                        Log.e("afterTextChanged ", "" + e)
                        canContinue = false
                    }
                }
            }

            hitApis() // fetchRequiredData
        } else {
            context?.toast("No recharge type defined for $rechargeTypeName !")
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun setupLocation() {
        try {
            if (GPSTracker(context).canGetLocation()) {
                latitude = "" + GPSTracker(context).latitude
                longitude = "" + GPSTracker(context).latitude
            } else {
                GPSTracker(context).showSettingsAlert()
            }
        } catch (e: Exception) {
            binding.root.snackbar("$e")
        }
    }

    private fun hitApis() {
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = com.app.ulife.creator.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "RC"
                )
            )
        )

        getPsFastagOperator(
            UserIdRequest(userid = "" + preferenceManager.userid)
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

            btnSubmit.onDebouncedListener {
                when {
                    etNumber.text.toString().isEmpty() -> etNumber.error = "Please enter number"
                    autoCompleteOperator.text.toString().isEmpty() -> autoCompleteOperator.error =
                        "Please select operator"

                    else -> {
                        if (canContinue) {
                            etAmount.error = "Please check your amount !"
                        } else {
                            getPsFetchFastagOperator(
                                GetPsFetchFastagReq(
                                    canumber = "" + etNumber.text,
                                    mode = "online",
                                    operator = "" + opId,
                                    userid = "" + preferenceManager.userid
                                )
                            )
                        }
                    }
                }
            }

            btnPay.setOnClickListener {
                when {
                    etNumber.text.toString().isEmpty() -> etNumber.error =
                        "Please enter vehicle number"

                    etAmount.text.toString().isEmpty() -> etAmount.error = "Please enter amount"
                    opId.isEmpty() -> root.snackbar("Select your operator")
                    latitude.isEmpty() -> {
                        context?.toast("Please turn on your location")
                        setupLocation()
                    }

                    longitude.isEmpty() -> {
                        context?.toast("Please turn on your location")
                        setupLocation()
                    }

                    else -> doPsFastagRecharge(
                        DoPsFastagRechargeReq(
                            amount = etAmount.text.toString().toDouble(),
                            billfetch = "",
                            canumber = "" + etNumber.text,
                            latitude = "" + latitude,
                            longitude = "" + longitude,
                            operator = opId.toInt(),
                            userid = "" + preferenceManager.userid
                        )
                    )
                }
            }
        }
    }

    private fun getWalletBalance(req: WalletReq) {
        viewModel.getWalletBalance = MutableLiveData()
        viewModel.getWalletBalance.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetWalletBalRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            tvWalletAmt.text = "" + response.data[0]?.Balance
                            walletAmt = response.data[0].Balance
                        }
                    } else {
                        binding.apply {
                            tvWalletAmt.text = "0.0"
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        tvWalletAmt.text = "0.0"
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    tvWalletAmt.text = "0.0"
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }

    private fun getPsFastagOperator(req: UserIdRequest) {
        viewModel.getPsFastagOperator = MutableLiveData()
        viewModel.getPsFastagOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPsFastagOpListRes::class.java)
                if (response != null) {
                    if (response.status) {
                        if (response.data.status) {
                            binding.apply {
                                nameArray.clear()
                                idArray.clear()
                                for (i in response.data.data.indices) {
                                    nameArray.add("" + response.data.data[i]?.name)
                                    idArray.add("" + response.data.data[i]?.id)
                                }
                                val operatorAdapter = ArrayAdapter(
                                    requireContext(), android.R.layout.simple_list_item_1, nameArray
                                )
                                autoCompleteOperator.setAdapter(operatorAdapter)
                                autoCompleteOperator.onItemClickListener =
                                    AdapterView.OnItemClickListener { parent, view, position, id ->
                                        val indexOfName =
                                            nameArray.indexOf(autoCompleteOperator.text.toString())
                                        opId = idArray[indexOfName]
                                        Log.e("opId ", "" + opId)
                                    }
                            }
                        } else {
                            (activity as MainActivity).apiErrorDialog(response.message)
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
        viewModel.getPsFastagOperator(req)
    }

    private fun getPsFetchFastagOperator(req: GetPsFetchFastagReq) {
        viewModel.getPsFetchFastagOperator = MutableLiveData()
        viewModel.getPsFetchFastagOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPsFetchFastagRes::class.java)
                if (response != null) {
                    if (response.status) {
//                        if (response.data.status) {
//                            binding.apply {}
//                        } else {
//                            (activity as MainActivity).apiErrorDialog(response.message)
//                        }
                    } else {
                        (activity as MainActivity).apiErrorDialog("" + response?.data)
                    }
                } else {
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getPsFetchFastagOperator(req)
    }

    private fun doPsFastagRecharge(req: DoPsFastagRechargeReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.doPsFastagRecharge = MutableLiveData()
        viewModel.doPsFastagRecharge.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, EmptyResponse::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        val dialog = Dialog(requireContext())
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.dialog_header_success)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                        val lp = WindowManager.LayoutParams()
                        lp.copyFrom(dialog.window?.attributes)
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

                        dialog.findViewById<MaterialButton>(R.id.btn_close)
                            .setOnClickListener {
                                Navigation.findNavController(binding.root).popBackStack()
                                dialog.dismiss()
                            }

                        dialog.findViewById<TextView>(R.id.tv_amount).text =
                            "Recharge successfully completed of ₹${binding.etAmount.text}, for operator ${binding.autoCompleteOperator.text}."
                        dialog.show()
                        dialog.window?.attributes = lp
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
        viewModel.doPsFastagRecharge(req)
    }
}