/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
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
import com.app.ulife.creator.databinding.FragmentRechargeCommonBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.EmptyResponse
import com.app.ulife.creator.models.Obj
import com.app.ulife.creator.models.bbpsRecharge.circle.GetCircleRes
import com.app.ulife.creator.models.bbpsRecharge.mobileLookup.GetMobileLookupReq
import com.app.ulife.creator.models.bbpsRecharge.mobileLookup.GetMobileLookupRes
import com.app.ulife.creator.models.bbpsRecharge.operator.GetOperatorReq
import com.app.ulife.creator.models.bbpsRecharge.operator.GetOperatorRes
import com.app.ulife.creator.models.bbpsRecharge.operatorLocal.GetOperatorLocalReq
import com.app.ulife.creator.models.bbpsRecharge.operatorLocal.GetOperatorLocalRes
import com.app.ulife.creator.models.fetchBill.FetchBillRes
import com.app.ulife.creator.models.recharge.DoRechargeNewReq
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.dthPlanList.DthListActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.PlanListActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.afterTextChanged
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class RechargeCommonFragment : Fragment(), KodeinAware {
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
    private var operatorSpKeyId = ""
    private var circleId = ""
    private var walletAmt = 0.0
    private val opListName = mutableListOf<String>()
    private val opListId = mutableListOf<String>()
    private val opListSpKey = mutableListOf<String>()
    private var fetchBillID = ""

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
                when (rechargeType) {
                    "Prepaid" -> {
                        etAmount.isFocusable = false
                        etNumber.afterTextChanged {
                            if (it.isNotEmpty()) {
                                if (it.length == 10) {
                                    doMobileLookUp(GetMobileLookupReq(accountno = "" + etNumber.text))
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

                    "Postpaid" -> {
                        etAmount.isFocusable = false
                        etNumber.afterTextChanged {
                            if (it.isNotEmpty()) {
                                if (it.length == 10) {
                                    doMobileLookUp(GetMobileLookupReq(accountno = "" + etNumber.text))
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

                    "DTH" -> {
                        etAmount.isFocusable = false
                        etlNumber.prefixText = ""
                        etlNumber.hint = "Operator Number"
                        etlNumber.counterMaxLength = 20
                        etNumber.filters = arrayOf(InputFilter.LengthFilter(20))

                        etNumber.afterTextChanged {
                            if (it.isNotEmpty()) {
                                if (it.length < 6) {
                                    doMobileLookUp(GetMobileLookupReq(accountno = "" + etNumber.text))
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

//                    "Electricity" -> {
//                        etlNumber.prefixText = ""
//                        etlNumber.hint = "Consumer ID"
//                        etlNumber.counterMaxLength = 10
//                        etNumber.filters = arrayOf(InputFilter.LengthFilter(10))
//                        llSelectPlan.visibility = View.GONE
//                        actlCircle.visibility = View.GONE
//                    }

                    else -> {
                        Log.e("rechargeTypeErr ", "undefined for --$rechargeType--")
                        etlNumber.prefixText = ""
                        etlNumber.hint = "Consumer ID"
                        etlNumber.counterMaxLength = 30
                        etNumber.filters = arrayOf(InputFilter.LengthFilter(30))
                        etNumber.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                        llSelectPlan.visibility = View.VISIBLE
                        tvPlanName.text = "Fetch Bill"
                        actlCircle.visibility = View.GONE
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
                apiname = "GetWalletBalance", obj = com.app.ulife.creator.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "RC"
                )
            )
        )

//        when (rechargeType) {
//            "Prepaid" -> getOperatorNew(
//                EmptyRequest(
//                    apiname = "GetOperatorCodes", obj = Obj(),
//                )
//            )
//
//            "DTH" -> getOperatorNew(
//                EmptyRequest(
//                    apiname = "GetOperatorCodes", obj = Obj(),
//                )
//            )
//
//            else -> getOperator(
//                GetOperatorReq(
//                    apiname = "GetRechargeOperator",
//                    obj = com.app.ulife.creator.models.bbpsRecharge.operator.Obj(Type = "$rechargeType")
//                )
//            )
//        }

        getOperator(
            GetOperatorReq(
                apiname = "GetRechargeOperator",
                obj = com.app.ulife.creator.models.bbpsRecharge.operator.Obj(Type = "$rechargeType")
            )
        )

        getCircle(
            EmptyRequest(
                apiname = "", obj = Obj()
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
                when (rechargeType) {
                    "Prepaid" -> {
                        when {
                            operatorId.isEmpty() -> actOperator.error =
                                "Please select your operator"

                            circleId.isEmpty() -> actCircle.error = "Please select your circle"
                            else -> {
                                val i = Intent(requireContext(), PlanListActivity::class.java)
                                i.putExtra("type", "mobile")
                                i.putExtra("circle", circleId)
                                i.putExtra("operator", operatorSpKeyId)
                                startActivityForResult(i, 69)
                                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                            }
                        }
                    }

                    "Postpaid" -> {
                        when {
                            operatorId.isEmpty() -> actOperator.error =
                                "Please select your operator"

                            circleId.isEmpty() -> actCircle.error = "Please select your circle"
                            else -> {
                                val i = Intent(requireContext(), PlanListActivity::class.java)
                                i.putExtra("type", "mobile")
                                i.putExtra("circle", circleId)
                                i.putExtra("operator", operatorSpKeyId)
                                startActivityForResult(i, 69)
                                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                            }
                        }
                    }

                    "DTH" -> {
                        when {
                            etNumber.text.toString().isEmpty() -> etNumber.error =
                                "Please enter Consumer ID"

                            operatorId.isEmpty() -> actOperator.error =
                                "Please select your operator"

                            circleId.isEmpty() -> actCircle.error = "Please select your circle"
                            else -> {
                                val i = Intent(requireContext(), DthListActivity::class.java)
                                i.putExtra("type", "mobile")
                                i.putExtra("circle", circleId)
                                i.putExtra("operator", operatorSpKeyId)
                                startActivityForResult(i, 69)
                                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                            }
                        }
                    }

                    else -> {
                        when {
                            operatorId.isEmpty() -> actOperator.error =
                                "Please select your operator"

                            else -> fetchBill(
                                DoRechargeNewReq(
                                    member_Id = "" + preferenceManager.userid,
                                    mobileNo = "" + etNumber.text,
                                    operatorID = "$operatorId",
                                    operatorName = "" + actOperator.text,
                                    operatorType = "FetchBill",
                                    rechargeAmount = "50",
                                    transactionPass = "" + etPass.text,
                                    walletAmount = "$walletAmt",
                                    walletType = "RC",
                                    planId = "" + planTypesId,
                                    FetchBillID = ""
                                )
                            )
                        }
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
                        doRecharge(
                            DoRechargeNewReq(
                                member_Id = "" + preferenceManager.userid,
                                mobileNo = "" + etNumber.text,
                                operatorID = "$operatorId",
                                operatorName = "" + actOperator.text,
                                operatorType = "$rechargeType",
                                rechargeAmount = "" + etAmount.text,
                                transactionPass = "" + etPass.text,
                                walletAmount = "$walletAmt",
                                walletType = "RC",
                                planId = "" + planTypesId,
                                FetchBillID = "" + fetchBillID,
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

    private fun doRecharge(req: DoRechargeNewReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.doRecharge = MutableLiveData()
        viewModel.doRecharge.observe(requireActivity()) {
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
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.doRecharge(req)
    }

    private fun getOperator(req: GetOperatorReq) {
        viewModel.getOperator = MutableLiveData()
        viewModel.getOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetOperatorRes::class.java)
                if (response != null) {
                    if (response.status) {
                        opListName.clear()
                        opListId.clear()
                        opListSpKey.clear()

                        for (i in response.data.indices) {
                            opListName.add("" + response.data[i].Service)
                            opListId.add("" + response.data[i].OperatorCode)
                            opListSpKey.add("" + response.data[i].PlanSPKey)
                        }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            opListName
                        )
                        binding.actOperator.setAdapter(adapter)
                        binding.actOperator.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                operatorId = opListId[position]
                                operatorSpKeyId = opListSpKey[position]
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
        viewModel.getOperator(req)
    }

    /*
    //    private fun getOperatorNew(req: EmptyRequest) {
    //        viewModel.getOperatorNew = MutableLiveData()
    //        viewModel.getOperatorNew.observe(requireActivity()) {
    //            try {
    //                val response = Gson().fromJson(it, GetOperatorListNewRes::class.java)
    //                if (response != null) {
    //                    if (response.status) {
    //                        opListName.clear()
    //                        opListId.clear()
    //                        opListSpKey.clear()
    //
    //                        if (!response.data.data.isNullOrEmpty()) {
    //                            for (i in response.data.data.indices) {
    //                                when (rechargeType) {
    //                                    "Prepaid" -> {
    //                                        if (response.data.data[i].serviceName.equals("Prepaid Info")) {
    //                                            opListName.add("" + response.data.data[i].operatorName)
    //                                            //opListId.add("" + response.data[i].OperatorCode)
    //                                            opListSpKey.add("" + response.data.data[i].spKey)
    //                                        } else {
    //                                            context?.toast("Operator not found !")
    //                                        }
    //                                    }
    //
    //                                    "DTH" -> {
    //                                        if (response.data.data[i].serviceName.equals("DTH Plan")) {
    //                                            opListName.add("" + response.data.data[i].operatorName)
    //                                            //opListId.add("" + response.data[i].OperatorCode)
    //                                            opListSpKey.add("" + response.data.data[i].spKey)
    //                                        } else {
    //                                            context?.toast("Operator not found !")
    //                                        }
    //                                    }
    //
    //                                    else -> context?.toast("Recharge type undefined !")
    //                                }
    //                            }
    //
    //                            val adapter = ArrayAdapter(
    //                                requireContext(),
    //                                android.R.layout.simple_list_item_1,
    //                                opListName
    //                            )
    //                            binding.actOperator.setAdapter(adapter)
    //                            binding.actOperator.onItemClickListener =
    //                                AdapterView.OnItemClickListener { parent, view, position, id ->
    //                                    operatorId = opListName[position]
    //                                    operatorSpKeyId = opListSpKey[position]
    //                                }
    //                        } else {
    //                            (activity as MainActivity).apiErrorDialog(it)
    //                        }
    //                    } else {
    //                        (activity as MainActivity).apiErrorDialog(response.message)
    //                    }
    //                } else {
    //                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
    //                }
    //            } catch (e: Exception) {
    //                (activity as MainActivity).apiErrorDialog("$it\n$e")
    //            }
    //        }
    //        viewModel.getOperatorNew(req)
    //    }
    */

    private fun getCircle(req: EmptyRequest) {
        LoadingUtils.showDialog(requireContext(), isCancelable = false)
        viewModel.getCircle = MutableLiveData()
        viewModel.getCircle.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCircleRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        val listName = mutableListOf<String>()
                        val listId = mutableListOf<String>()

                        listName.clear()
                        listId.clear()

                        for (i in response.data.data.indices) {
                            listName.add("" + response.data.data[i].circleName)
                            listId.add("" + response.data.data[i].circleCode)
                        }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            listName
                        )
                        binding.actCircle.setAdapter(adapter)
                        binding.actCircle.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                circleId = listId[position]
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
        viewModel.getCircle(req)
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

    private fun doMobileLookUp(req: GetMobileLookupReq) {
        viewModel.doMobileLookUp = MutableLiveData()
        viewModel.doMobileLookUp.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetMobileLookupRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            when (response.data.rootNode[0].statusCode) {
                                "1" -> {
                                    actOperator.setText("" + response.data.rootNode[0].operator)
                                    actlOperator.isEnabled = false
                                    operatorId = "" + response.data.rootNode[0].spkey
                                    for (i in opListId.indices) {
                                        println("$operatorId ---- ${opListId[i]}")
                                        when {
                                            operatorId.equals(opListId[i]) -> {
                                                operatorSpKeyId = opListSpKey[i]
                                                break
                                            }

                                            else -> {
                                                getOperatorLocal(
                                                    GetOperatorLocalReq(
                                                        apiname = "GetRechargeOperator",
                                                        obj = com.app.ulife.creator.models.bbpsRecharge.operatorLocal.Obj(
                                                            Type = "" + rechargeType,
                                                            name = "" + response.data.rootNode[0].operator
                                                        )
                                                    )
                                                )
//                                                actOperator.setText("")
//                                                actlOperator.isEnabled = true
//                                                actCircle.setText("")
//                                                actlCircle.isEnabled = true
//                                                if (i + 1 == opListId.size && operatorId.equals("5905")) {
//                                                    // end
//                                                    context?.toast("Couldn't find operator, please select manually !")
//                                                }
                                            }
                                        }
                                    }
                                    actCircle.setText("" + response.data.rootNode[0].circle)
                                    actlCircle.isEnabled = false
                                    circleId = "" + response.data.rootNode[0].circleCode
                                }

                                else -> {
                                    (activity as MainActivity).apiErrorDialog("${response.data.rootNode[0]?.msg}")
                                }
                            }
                        }
                    } else {
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
                    binding.apply {
                        actOperator.setText("")
                        actlOperator.isEnabled = true
                        actCircle.setText("")
                        actlCircle.isEnabled = true
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    actOperator.setText("")
                    actlOperator.isEnabled = true
                    actCircle.setText("")
                    actlCircle.isEnabled = true
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.doMobileLookUp(req)
    }

    private fun getOperatorLocal(req: GetOperatorLocalReq) {
        viewModel.getOperatorLocal = MutableLiveData()
        viewModel.getOperatorLocal.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetOperatorLocalRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            if (!response.data.isNullOrEmpty()) {
                                actOperator.setText("" + response.data[0].Service)
                                actlOperator.isEnabled = false
                                operatorId = "" + response.data[0].OperatorCode
                                operatorSpKeyId = "" + response.data[0].PlanSPKey

                            } else {
                                actOperator.setText("")
                                actlOperator.isEnabled = true
                                actCircle.setText("")
                                actlCircle.isEnabled = true
                                context?.toast("Couldn't find operator, please select manually !")
                            }
                        }
                    } else {
                        (activity as MainActivity).apiErrorDialog(it)
                    }
                } else {
                    (activity as MainActivity).apiErrorDialog("" + response?.message)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getOperatorLocal(req)
    }

    private fun fetchBill(req: DoRechargeNewReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.fetchBill = MutableLiveData()
        viewModel.fetchBill.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, FetchBillRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        fetchBillID = response.data[0].table[0].fetchBillID
                        binding.apply {
                            tvPlanDesc.visibility = View.VISIBLE
                            tvPlanDesc.text =
                                "Name : ${response.data[0].table[0].name.ifEmpty { " N/A" }}\n" +
                                        "Service Name : ${
                                            response.data[0].table[0]?.serviceName.toString()
                                                .ifEmpty { " N/A" }
                                        }\n" +
                                        "RPID : ${
                                            response.data[0].table[0]?.rpid.toString()
                                                .ifEmpty { " N/A" }
                                        }\n" +
                                        "Balance : ${
                                            response.data[0].table[0]?.bal.toString()
                                                .ifEmpty { " N/A" }
                                        }\n" +
                                        "Due Date : ${
                                            response.data[0].table[0]?.duedate.toString()
                                                .ifEmpty { " N/A" }
                                        }\n" +
                                        "Due Amount : ${
                                            response.data[0].table[0]?.dueamount.toString()
                                                .ifEmpty { " N/A" }
                                        }"

                            etAmount.setText(
                                "${
                                    response.data[0].table[0]?.dueamount.toString()
                                        .ifEmpty { "0.0" }
                                }"
                            )
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
        viewModel.fetchBill(req)
    }
}