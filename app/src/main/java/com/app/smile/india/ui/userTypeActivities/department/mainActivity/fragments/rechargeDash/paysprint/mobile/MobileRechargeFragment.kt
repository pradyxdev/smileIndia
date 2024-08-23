/*
 * *
 *  * Created by Prady on 4/20/24, 2:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 10:26 AM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.rechargeDash.paysprint.mobile

import android.app.Dialog
import android.content.Intent
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
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentRechargeCommonBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.EmptyResponse
import com.app.smile.india.models.UserIdRequest
import com.app.smile.india.models.paysprint.doPsRecharge.DoPsRechargeReq
import com.app.smile.india.models.paysprint.hlrCheck.HlrCheckReq
import com.app.smile.india.models.paysprint.hlrCheck.HlrCheckRes
import com.app.smile.india.models.paysprint.psOpList.GetPsOpListRes
import com.app.smile.india.models.wallet.WalletReq
import com.app.smile.india.models.wallet.balance.GetWalletBalRes
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.MobPlanListActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.afterTextChanged
import com.app.smile.india.utils.onDebouncedListener
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class MobileRechargeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentRechargeCommonBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var rechargeTypeName = ""
    private var rechargeType = ""
    private var operatorId = ""
    private var planTypesId = ""
    private var circleId = ""
    private var walletAmt = 0.0
    private val opListName = mutableListOf<String>()
    private val opListId = mutableListOf<String>()
    private var fetchBillID = ""
    private var hlrOperator = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRechargeCommonBinding.inflate(layoutInflater)
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

        if (!rechargeType.isNullOrEmpty()) {
            Log.e("rechargeType ", "--$rechargeType--")

            binding.apply {
                etAmount.isFocusable = false
                etNumber.afterTextChanged {
                    if (it.isNotEmpty()) {
                        if (it.length == 10) {
                            hlrCheck(
                                HlrCheckReq(
                                    number = "" + etNumber.text,
                                    type = "mobile",
                                    userid = "" + preferenceManager.userid
                                )
                            )
                        } else {
                            actOperator.setText("")
                            actlOperator.isEnabled = true
                            actCircle.setText("")
                            actlCircle.isEnabled = true
                        }
                    } else {
                        actOperator.setText("")
                        actlOperator.isEnabled = true
                        actCircle.setText("")
                        actlCircle.isEnabled = true
                    }
                }
            }

            hitApis() // fetchRequiredData
        } else {
            context?.toast("No recharge type defined for $rechargeTypeName !")
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun hitApis() {
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = com.app.smile.india.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "RC"
                )
            )
        )

        getPsOperator(
            UserIdRequest(userid = "" + preferenceManager.userid)
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            Constants.gstPaysprintStateNameArray.sorted()
        )
        binding.actCircle.setAdapter(adapter)
        binding.actCircle.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val index =
                    Constants.gstPaysprintStateNameArray.indexOf("" + binding.actCircle.text)
                circleId = Constants.gstPaysprintStateNameArray[index]
                println("circleId : $circleId")
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

            etAmount.afterTextChanged {
                if (it.isNotEmpty()) {
                    if (etAmount.text.toString().toDouble() > walletAmt) {
                        context?.toast("Insufficient fund !")
                        etAmount.error = "Insufficient fund !"
                        btnSubmit.isEnabled = false
                    } else if (etAmount.text.toString().equals("0")
                        || etAmount.text.toString().equals("0.0")
                        || etAmount.text.toString().equals("0.00")
                    ) {
                        etAmount.error = "Amount can't be zero !"
                        btnSubmit.isEnabled = false
                    } else {
                        btnSubmit.isEnabled = true
                        etAmount.error = null
                    }
                } else {
                    btnSubmit.isEnabled = true
                    etAmount.error = null
                }
            }

            cardPlans.setOnClickListener {
                when {
                    actOperator.text.toString().isEmpty() -> actOperator.error =
                        "Please select your operator"

                    actCircle.text.toString().isEmpty() -> actCircle.error =
                        "Please select your circle"

                    else -> {
                        val opName = "" + actOperator.text
                        val opCircle = "" + actCircle.text

                        val i = Intent(requireContext(), MobPlanListActivity::class.java)
                        i.putExtra("type", "mobile")
                        i.putExtra("circle", opCircle)
//                        i.putExtra("operator", operatorId)
                        i.putExtra("operator", opName)
                        startActivityForResult(i, 69)
                        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                }
            }

            btnSubmit.onDebouncedListener {
                when {
                    operatorId.isEmpty() -> actOperator.error = "Please select your operator"
                    etNumber.text.toString().isEmpty() -> etNumber.error = "Please enter number"
                    etAmount.text.toString().isEmpty() -> etAmount.error = "Please enter amount"
//                    etPass.text.toString().isEmpty() -> etAmount.error = "Please enter password"
                    else -> {
                        doPsMobRecharge(
                            DoPsRechargeReq(
                                amount = "" + etAmount.text,
                                canumber = "" + etNumber.text,
                                operator = "" + operatorId,
                                userid = "" + preferenceManager.userid,
                                OperatorName = "" + actOperator.text,
                                Circle = "" + actCircle.text,
                                PlanDescription = "" + tvPlanDesc.text

//                                member_Id = "" + preferenceManager.userid,
//                                mobileNo = "" + etNumber.text,
//                                operatorID = "$operatorId",
//                                operatorName = "" + actOperator.text,
//                                operatorType = "$rechargeType",
//                                rechargeAmount = "" + etAmount.text,
//                                transactionPass = "" + etPass.text,
//                                walletAmount = "$walletAmt",
//                                walletType = "RC",
//                                planId = "" + planTypesId,
//                                FetchBillID = "" + fetchBillID,
                            )
                        )
                    }
                }
            }
        }
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 69) {
            Log.e("onResultRec ", "intent = " + data?.getStringExtra("amount"))
            binding.etAmount.setText(data?.getStringExtra("amount"))
            binding.tvPlanDesc.visibility = View.VISIBLE
            binding.tvPlanDesc.text = data?.getStringExtra("desc")
            planTypesId = "" + data?.getStringExtra("typesId")
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getWalletBalance(req)
    }

    private fun getPsOperator(req: UserIdRequest) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getPsOperator = MutableLiveData()
        viewModel.getPsOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPsOpListRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        opListName.clear()
                        opListId.clear()

                        if (response.data.status) {
                            for (i in response.data.data.indices) {
                                if (response.data.data[i].category.equals(
                                        "Prepaid",
                                        ignoreCase = true
                                    )
                                ) {
                                    opListName.add("" + response.data.data[i].name)
                                    opListId.add("" + response.data.data[i].id)
                                }
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                opListName
                            )
                            binding.actOperator.setAdapter(adapter)
                            binding.actOperator.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    val index = opListName.indexOf("" + binding.actOperator.text)
                                    operatorId = opListId[index]
                                    println("operatorId : $operatorId")
                                }

                            if (!hlrOperator.isNullOrEmpty() && opListName.contains(hlrOperator)) {
                                val index = opListName.indexOf(hlrOperator)
                                operatorId = opListId[index]
                                println("operatorId : $operatorId")
                            } else {
                                binding.actOperator.setText("")
                                binding.actCircle.setText("")
                                println("Operator doesn't exist !")
                            }
                        } else {
                            LoadingUtils.hideDialog()
                            (activity as MainActivity).apiErrorDialog(response.message)
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
        viewModel.getPsOperator(req)
    }

    private fun hlrCheck(req: HlrCheckReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.hlrCheck = MutableLiveData()
        viewModel.hlrCheck.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, HlrCheckRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            if (response.data.status) {
                                actOperator.setText("" + response?.data?.info?.operator)

                                val circle = response?.data?.info?.circle.toString().trim()

                                if (!circle.isNullOrEmpty() && circle.equals("Jammu and Kashmir")) {
                                    actCircle.setText("Jammu Kashmir")
                                } else if (!circle.isNullOrEmpty() && circle.equals("Maharashtra and Goa")) {
                                    actCircle.setText("Maharashtra Goa")
                                } else if (!circle.isNullOrEmpty() && circle.equals("Maharashtra")) {
                                    actCircle.setText("Maharashtra Goa")
                                } else if (!circle.isNullOrEmpty() && circle.equals("null")) {
                                    actCircle.setText("")
                                } else {
                                    actCircle.setText("" + response?.data?.info?.circle)
                                }

                                circleId = "" + response?.data?.info?.circle
                                hlrOperator = "" + response?.data?.info?.operator

                                getPsOperator(
                                    UserIdRequest(userid = "" + preferenceManager.userid)
                                )
                            } else {
                                (activity as MainActivity).apiErrorDialog("${response.data.message}")
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            actOperator.setText("")
                            actlOperator.isEnabled = true
                            actCircle.setText("")
                            actlCircle.isEnabled = true
                        }
                        context?.toast("" + response.message)
//                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.apply {
                        actOperator.setText("")
                        actlOperator.isEnabled = true
                        actCircle.setText("")
                        actlCircle.isEnabled = true
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.apply {
                    actOperator.setText("")
                    actlOperator.isEnabled = true
                    actCircle.setText("")
                    actlCircle.isEnabled = true
                }
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.hlrCheck(req)
    }

    private fun doPsMobRecharge(req: DoPsRechargeReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.doPsMobRecharge = MutableLiveData()
        viewModel.doPsMobRecharge.observe(requireActivity()) {
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
                            "Recharge successfully completed of ₹${binding.etAmount.text}, for operator ${binding.actOperator.text}."
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.doPsMobRecharge(req)
    }
}