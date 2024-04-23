/*
 * *
 *  * Created by Prady on 4/20/24, 2:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 10:26 AM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.bbps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creator.databinding.FragmentBbpsPayBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.UserIdRequest
import com.app.ulife.creator.models.paysprint.bbpsFetchBill.GetPsFetchBbpsReq
import com.app.ulife.creator.models.paysprint.bbpsOpList.DataX
import com.app.ulife.creator.models.paysprint.bbpsOpList.GetBbpsOpListRes
import com.app.ulife.creator.models.paysprint.fastag.psFetchFastag.GetPsFetchFastagRes
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class BbpsRechargeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentBbpsPayBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var rechargeTypeName = ""
    private var rechargeType = ""
    private var walletAmt = 0.0
    private var categoryName = ArrayList<String>()
    private var opId = ""
    var categoryId = ""
    var categoryIdArr = ArrayList<String>()
    var categoryOpName = ArrayList<String>()

    var selectedOpData = ArrayList<DataX>()
    var arrPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBbpsPayBinding.inflate(layoutInflater)
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
//                etNumber.afterTextChanged {
//                    if (it.isNotEmpty()) {
//                        if (it.length == 10) {
//                            hlrCheck(
//                                HlrCheckReq(
//                                    number = "" + etNumber.text,
//                                    type = "dth",
//                                    userid = "" + preferenceManager.userid
//                                )
//                            )
//                        } else {
//                            actOperator.setText("")
//                            actlOperator.isEnabled = true
//                            actCircle.setText("")
//                            actlCircle.isEnabled = true
//                        }
//                    } else {
//                        actOperator.setText("")
//                        actlOperator.isEnabled = true
//                        actCircle.setText("")
//                        actlCircle.isEnabled = true
//                    }
//                }
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

        getPsBbpsOperator(
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

            btnFetch.onDebouncedListener {
                when {
                    categoryOpName.isEmpty() -> binding.autoCompleteOperator.error =
                        "Please select a existing operator !"

                    categoryId.isEmpty() -> binding.autoCompleteCategory.error =
                        "Please select a existing category !"

                    else -> {
                        getPsFetchBbpsOperator(
                            GetPsFetchBbpsReq(
                                ad1 = "" + etAd1.text,
                                ad2 = "" + etAd2.text,
                                ad3 = "" + etAd3.text,
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

    private fun getPsBbpsOperator(req: UserIdRequest) {
        viewModel.getPsBbpsOperator = MutableLiveData()
        viewModel.getPsBbpsOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetBbpsOpListRes::class.java)
                if (response != null) {
                    if (response.status) {
                        if (response.data.status) {
                            binding.apply {
                                categoryOpName.clear()
                                categoryIdArr.clear()
                                selectedOpData.clear()

                                // todo: remove
                                categoryName.clear()
                                for (i in response.data.data.indices) {
                                    categoryName.add("" + response.data.data[i]?.category)
                                }

                                val categoryNameAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    categoryName.filterNotNull().distinct()
                                )
                                autoCompleteCategory.setAdapter(categoryNameAdapter)
                                // todo: till here

                                for (i in response.data.data.indices) {
                                    if (rechargeType.equals("Postpaid")) {
                                        if ("Postpaid".equals(
                                                "" + response.data.data[i]?.category,
                                                ignoreCase = true
                                            ) || "POST PAID".equals(
                                                "" + response.data.data[i]?.category,
                                                ignoreCase = true
                                            )
                                        ) {
                                            categoryOpName.add("" + response.data.data[i]?.name)
                                            categoryIdArr.add("" + response.data.data[i]?.id)
                                            selectedOpData.addAll(response.data.data)
                                        } else {
                                            Log.e(
                                                "rechargeType ",
                                                "--doesn't exist in list-->$rechargeType"
                                            )
                                        }
                                    } else {
                                        if (rechargeType.equals(
                                                "" + response.data.data[i]?.category,
                                                ignoreCase = true
                                            )
                                        ) {
                                            categoryOpName.add("" + response.data.data[i]?.name)
                                            categoryIdArr.add("" + response.data.data[i]?.id)
                                            selectedOpData.addAll(response.data.data)
                                        } else {
                                            Log.e(
                                                "rechargeType ",
                                                "--doesn't exist in list-->$rechargeType"
                                            )
                                        }
                                    }
                                }

                                val categoryOpNameAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    categoryOpName
                                )
                                autoCompleteOperator.setAdapter(categoryOpNameAdapter)
                                autoCompleteOperator.onItemClickListener =
                                    AdapterView.OnItemClickListener { parent, view, position, id ->
                                        // todo : enable it if view bill is 0 in any case
//                                        if (selectedOpData[position].viewbill.equals("1")) {
                                        val indexOfName =
                                            categoryOpName.indexOf(autoCompleteOperator.text.toString())
                                        categoryId = categoryIdArr[indexOfName]
                                        opId = categoryIdArr[indexOfName]
                                        Log.e(
                                            "opId ",
                                            "" + categoryIdArr[position] + " // " + "$categoryId"
                                        )
//                                    categoryId = categoryIdArr[position]
                                        setupUi(position)
//                                        } else {
//                                            etlBillNo.hint = selectedOpData[position].displayname
//                                            etlBillNo.visibility = View.VISIBLE
//
//                                            btnSubmit.text = "Pay now"
//                                        }
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
        viewModel.getPsBbpsOperator(req)
    }

    private fun setupUi(position: Int) {
        arrPosition = position

        binding.apply {
            if (!selectedOpData[position].displayname.isNullOrEmpty() && !selectedOpData[position].displayname.equals(
                    "null"
                )
            ) {
                etlBillNo.hint = selectedOpData[position].displayname
                etlBillNo.visibility = View.VISIBLE
//                println(selectedOpData[position].regex)
//                etNumber.addTextChangedListener(RegexMaskTextWatcher(selectedOpData[position].regex))
            } else {
                etlBillNo.hint = "Bill number"
                etNumber.setText("")
                etlBillNo.visibility = View.VISIBLE
            }
            if (!selectedOpData[position].ad1_d_name.isNullOrEmpty() && !selectedOpData[position].ad1_d_name.equals(
                    "null"
                )
            ) {
                etlAd1.hint = selectedOpData[position].ad1_d_name
                etlAd1.visibility = View.VISIBLE
            } else {
                etAd1.setText("")
                etlAd1.visibility = View.GONE
            }
            if (!selectedOpData[position].ad2_d_name.isNullOrEmpty() && !selectedOpData[position].ad2_d_name.equals(
                    "null"
                )
            ) {
                etlAd2.hint = selectedOpData[position].ad2_d_name
                etlAd2.visibility = View.VISIBLE
            } else {
                etAd2.setText("")
                etlAd2.visibility = View.GONE
            }
            if (!selectedOpData[position].ad3_d_name.isNullOrEmpty() && !selectedOpData[position].ad3_d_name.equals(
                    "null"
                )
            ) {
                etlAd3.hint = selectedOpData[position].ad3_d_name
                etlAd3.visibility = View.VISIBLE
            } else {
                etAd3.setText("")
                etlAd3.visibility = View.GONE
            }
        }
    }

    private fun getPsFetchBbpsOperator(req: GetPsFetchBbpsReq) {
        viewModel.getPsFetchBbpsOperator = MutableLiveData()
        viewModel.getPsFetchBbpsOperator.observe(requireActivity()) {
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
        viewModel.getPsFetchBbpsOperator(req)
    }

//    private fun doPsFastagRecharge(req: DoPsFastagRechargeReq) {
//        LoadingUtils.showDialog(context, isCancelable = false)
//        viewModel.doPsFastagRecharge = MutableLiveData()
//        viewModel.doPsFastagRecharge.observe(requireActivity()) {
//            try {
//                val response = Gson().fromJson(it, EmptyResponse::class.java)
//                if (response != null) {
//                    if (response.status) {
//                        LoadingUtils.hideDialog()
//                        val dialog = Dialog(requireContext())
//                        dialog.setCancelable(false)
//                        dialog.setContentView(R.layout.dialog_header_success)
//                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                        val lp = WindowManager.LayoutParams()
//                        lp.copyFrom(dialog.window?.attributes)
//                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//
//                        dialog.findViewById<MaterialButton>(R.id.btn_close)
//                            .setOnClickListener {
//                                Navigation.findNavController(binding.root).popBackStack()
//                                dialog.dismiss()
//                            }
//
//                        dialog.findViewById<TextView>(R.id.tv_amount).text =
//                            "Recharge successfully completed of ₹${binding.etAmount.text}, for operator ${binding.autoCompleteOperator.text}."
//                        dialog.show()
//                        dialog.window?.attributes = lp
//                    } else {
//                        LoadingUtils.hideDialog()
//                        (activity as MainActivity).apiErrorDialog(response.message)
//                    }
//                } else {
//                    LoadingUtils.hideDialog()
//                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
//                }
//            } catch (e: Exception) {
//                LoadingUtils.hideDialog()
//                (activity as MainActivity).apiErrorDialog("$it\n$e")
//            }
//        }
//        viewModel.doPsFastagRecharge(req)
//    }
}