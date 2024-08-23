/*
 * *
 *  * Created by Prady on 4/20/24, 2:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 10:26 AM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.dth

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
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentDthRechargeBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.EmptyResponse
import com.app.smile.india.models.UserIdRequest
import com.app.smile.india.models.paysprint.doPsRecharge.DoPsRechargeReq
import com.app.smile.india.models.paysprint.dthHlr.GetHlrDthInfoReq
import com.app.smile.india.models.paysprint.dthHlr.GetHlrDthInfoRes
import com.app.smile.india.models.paysprint.psOpList.GetPsOpListRes
import com.app.smile.india.models.wallet.WalletReq
import com.app.smile.india.models.wallet.balance.GetWalletBalRes
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.afterTextChanged
import com.app.smile.india.utils.hideKeyboard
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

class DthRechargeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentDthRechargeBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var rechargeTypeName = ""
    private var rechargeType = ""
    private var operatorId = ""
    private var walletAmt = 0.0
    private val opListName = mutableListOf<String>()
    private val opListId = mutableListOf<String>()
    private var hlrOperator = ""

    private var staticOperators = listOf("Airteldth", "TataSky", "Videocon", "Sundirect", "Dishtv")
    private var staticOperatorsId = listOf("12", "8", "10", "27", "14")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDthRechargeBinding.inflate(layoutInflater)
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

//        getPsOperator(
//            UserIdRequest(userid = "" + preferenceManager.userid)
//        )
        // static operators

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            staticOperators
        )
        binding.actOperator.setAdapter(adapter)
        binding.actOperator.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val index = staticOperators.indexOf("" + binding.actOperator.text)
                operatorId = staticOperatorsId[index]
                println("operatorId : $operatorId")
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

            btnFetchHlrInfo.setOnClickListener {
                when {
                    etNumber.text.toString().isEmpty() -> etNumber.error = "Please enter your id"
                    actOperator.text.toString().isEmpty() -> actOperator.error =
                        "Please select operator"

                    else -> {
                        hideKeyboard()
                        hlrCheck(
                            GetHlrDthInfoReq(
                                canumber = "" + etNumber.text,
                                op = "" + actOperator.text,
                                userid = "" + preferenceManager.userid
                            )
                        )
                    }
                }
            }

            btnSubmit.onDebouncedListener {
                when {
                    etNumber.text.toString().isEmpty() -> etNumber.error = "Please enter your id"
                    actOperator.text.toString().isEmpty() -> actOperator.error =
                        "Please select operator"

                    etAmount.text.toString().isEmpty() -> etAmount.error = "Please enter amount"
                    else -> {
                        doPsMobRecharge(
                            DoPsRechargeReq(
                                amount = "" + etAmount.text,
                                canumber = "" + etNumber.text,
                                operator = "" + operatorId,
                                userid = "" + preferenceManager.userid,
                                OperatorName = "" + actOperator.text,
                                Circle = "",
                                PlanDescription = "" + tvHlrPlan.text
                            )
                        )
                    }
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
                                        "DTH",
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

    private fun hlrCheck(req: GetHlrDthInfoReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getHlrDthInfo = MutableLiveData()
        viewModel.getHlrDthInfo.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetHlrDthInfoRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            if (response.data.status) {
                                cardHlrInfo.visibility = View.VISIBLE
                                tvHlrName.text = "" + response.data.info[0]?.customerName
                                tvHlrBalance.text = "" + response.data.info[0]?.balance
                                tvHlrStatus.text = "" + response.data.info[0]?.status
                                tvHlrDueDate.text = "" + response.data.info[0]?.nextRechargeDate
                                tvHlrPlan.text = "" + response.data.info[0]?.planname
                                tvHlrPrice.text = "" + response.data.info[0]?.monthlyRecharge
                                etAmount.setText("" + response.data.info[0]?.monthlyRecharge)
                            } else {
                                cardHlrInfo.visibility = View.GONE
                                (activity as MainActivity).apiErrorDialog("${response.data.message}")
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            cardHlrInfo.visibility = View.GONE
                            tvHlrName.text = ""
                            tvHlrBalance.text = ""
                            tvHlrStatus.text = ""
                            tvHlrDueDate.text = ""
                            tvHlrPlan.text = ""
                            tvHlrPrice.text = ""
                            etAmount.setText("")
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.apply {
                        cardHlrInfo.visibility = View.GONE
                        tvHlrName.text = ""
                        tvHlrBalance.text = ""
                        tvHlrStatus.text = ""
                        tvHlrDueDate.text = ""
                        tvHlrPlan.text = ""
                        tvHlrPrice.text = ""
                        etAmount.setText("")
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.apply {
                    cardHlrInfo.visibility = View.GONE
                    tvHlrName.text = ""
                    tvHlrBalance.text = ""
                    tvHlrStatus.text = ""
                    tvHlrDueDate.text = ""
                    tvHlrPlan.text = ""
                    tvHlrPrice.text = ""
                    etAmount.setText("")
                }
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getHlrDthInfo(req)
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