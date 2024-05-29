/*
 * *
 *  * Created by Prady on 4/20/24, 2:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 10:26 AM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.lpg

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
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.FragmentLpgPayBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.Coroutines
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.EmptyRequest
import com.app.ulife.creatoron.models.EmptyResponse
import com.app.ulife.creatoron.models.UserIdRequest
import com.app.ulife.creatoron.models.paysprint.bbpsOpList.DataX
import com.app.ulife.creatoron.models.paysprint.bbpsOpList.GetBbpsOpListRes
import com.app.ulife.creatoron.models.paysprint.lpg.fetchBill.GetPsFetchLpgBillReq
import com.app.ulife.creatoron.models.paysprint.lpg.fetchBill.GetPsFetchLpgBillRes
import com.app.ulife.creatoron.models.paysprint.lpg.fetchDistributer.GetLpgDistributerRes
import com.app.ulife.creatoron.models.paysprint.lpg.fetchDistrict.GetLpgDistrictReq
import com.app.ulife.creatoron.models.paysprint.lpg.fetchDistrict.GetLpgDistrictRes
import com.app.ulife.creatoron.models.paysprint.lpg.fetchDistrict.Obj
import com.app.ulife.creatoron.models.paysprint.lpg.fetchState.GetLpgStateListRes
import com.app.ulife.creatoron.models.paysprint.lpg.payLpgBill.GetPsLpgPayBillReq
import com.app.ulife.creatoron.models.wallet.WalletReq
import com.app.ulife.creatoron.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.GPSTracker
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.afterTextChanged
import com.app.ulife.creatoron.utils.hideKeyboard
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.utils.snackbar
import com.app.ulife.creatoron.utils.toast
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class LpgRechargeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentLpgPayBinding
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
    private lateinit var billFetchData: Any
    private var latitude = ""
    private var longitude = ""
    private var selectedLpgState = ""
    private var selectedLpgDistrict = ""
    private var selectedLpgDistributer = ""
    private var selectedMode = ""
//    private var modeNameList = listOf("Mode 1", "Mode 2", "Mode 3")
//    private var modeIdList = listOf("1", "2", "3")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLpgPayBinding.inflate(layoutInflater)
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
            hitApis() // fetchRequiredData
        } else {
            context?.toast("No recharge type defined for $rechargeTypeName !")
            Navigation.findNavController(binding.root).popBackStack()
        }

//        val categoryNameAdapter = ArrayAdapter(
//            requireContext(),
//            android.R.layout.simple_list_item_1,
//            modeNameList
//        )
//        binding.actMode.setAdapter(categoryNameAdapter)
//        binding.actMode.setOnItemClickListener { adapterView, view, position, l ->
//            selectedMode = modeIdList[position]
//        }

        Coroutines.main {
            delay(1500)
            setupLocation()
        }
    }

    private fun hitApis() {
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = com.app.ulife.creatoron.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "RC"
                )
            )
        )

        getLpgBbpsOperator(
            UserIdRequest(userid = "" + preferenceManager.userid)
        )

        getLpgStateList(
            EmptyRequest(
                apiname = "GetLPGStateList",
                obj = com.app.ulife.creatoron.models.Obj()
            )
        )
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

            btnFindDistributer.setOnClickListener {
                (activity as MainActivity).openWebIntent("https://dcmstransparency.hpcl.co.in/myhpgas/hpgas/locatedistributor.aspx")
            }

            etAmount.afterTextChanged {
                if (it.isNotEmpty()) {
                    if (etAmount.text.toString().toDouble() > walletAmt) {
                        context?.toast("Insufficient fund !")
                        etAmount.error = "Insufficient fund !"
                        btnPay.isEnabled = false
                    } else if (etAmount.text.toString().equals("0")
                        || etAmount.text.toString().equals("0.0")
                        || etAmount.text.toString().equals("0.00")
                    ) {
                        etAmount.error = "Amount can't be zero !"
                        btnPay.isEnabled = false
                    } else {
                        btnPay.isEnabled = true
                        etAmount.error = null
                    }
                } else {
                    btnPay.isEnabled = true
                    etAmount.error = null
                }
            }

            btnFetch.onDebouncedListener {
                when {
                    categoryOpName.isEmpty() -> binding.autoCompleteOperator.error =
                        "Please select a existing operator !"

                    categoryId.isEmpty() -> binding.autoCompleteCategory.error =
                        "Please select a existing category !"

                    latitude.isEmpty() -> {
                        context?.toast("Please turn on your location")
                        setupLocation()
                    }

                    longitude.isEmpty() -> {
                        context?.toast("Please turn on your location")
                        setupLocation()
                    }

                    else -> {
                        hideKeyboard()
                        getPsFetchLpgOperator(
                            GetPsFetchLpgBillReq(
                                Ad1 = "" + selectedMode,
                                Ad2 = "" + etAd1.text,
                                Ad3 = "" + etAd2.text,
                                Ad4 = "" + etAd3.text,
                                canumber = "" + etNumber.text,
                                operator = opId.toInt(),
                                userid = "" + preferenceManager.userid,
                                latitude = "$latitude",
                                longitude = "$longitude",
                                OperatorName = "" + autoCompleteOperator.text,
                                Circle = "" + autoCompleteCategory.text,
                                PlanDescription = ""
                            )
                        )
                    }
                }
            }

            btnPay.onDebouncedListener {
                doPsLpgBillPay(
                    GetPsLpgPayBillReq(
                        userid = "" + preferenceManager.userid,
                        canumber = "" + etNumber.text,
                        amount = etAmount.text.toString().toDouble(),
                        latitude = "" + latitude,
                        longitude = "" + longitude,
                        mode = "online",
                        operator = opId.toInt(),
                        Circle = "",
                        PlanDescription = "LPG of ${autoCompleteOperator.text} for customer ${etNumber.text}-${etAd1.text}-${etAd2.text}-${etAd3.text} of amount ${etAmount.text}",
                        ad1 = "" + selectedMode,
                        ad2 = "" + etAd1.text,
                        ad3 = "" + etAd2.text,
                        ad4 = "" + etAd3.text,
                        OperatorName = "" + autoCompleteOperator.text
                    )

//                    GetPsBbpsPayBillReq(
//                        userid = "" + preferenceManager.userid,
//                        canumber = "" + etNumber.text,
//                        amount = etAmount.text.toString().toDouble(),
//                        latitude = "" + latitude,
//                        longitude = "" + longitude,
//                        mode = "online",
//                        operator = opId.toInt(),
//                        bill_fetch = billFetchData,
//                        Circle = "",
//                        PlanDescription = "BBPS of ${autoCompleteOperator.text} for customer ${etNumber.text}-${etAd1.text}-${etAd2.text}-${etAd3.text} of amount ${etAmount.text}"
//                    )
                )
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

    private fun getLpgBbpsOperator(req: UserIdRequest) {
        viewModel.getLpgBbpsOperator = MutableLiveData()
        viewModel.getLpgBbpsOperator.observe(requireActivity()) {
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
                                    categoryOpName.add("" + response.data.data[i]?.name)
                                    categoryIdArr.add("" + response.data.data[i]?.id)
                                    selectedOpData.add(
                                        DataX(
                                            ad1_d_name = "" + response.data.data[i]?.ad1_d_name,
                                            ad1_name = "" + response.data.data[i]?.ad1_name,
                                            ad1_regex = "" + response.data.data[i]?.ad1_regex,
                                            ad2_d_name = "" + response.data.data[i]?.ad2_d_name,
                                            ad2_name = "" + response.data.data[i]?.ad2_name,
                                            ad2_regex = "" + response.data.data[i]?.ad2_regex,
                                            ad3_d_name = "" + response.data.data[i]?.ad3_d_name,
                                            ad3_name = "" + response.data.data[i]?.ad3_name,
                                            ad3_regex = "" + response.data.data[i]?.ad3_regex,
                                            category = "" + response.data.data[i]?.category,
                                            displayname = "" + response.data.data[i]?.displayname,
                                            id = "" + response.data.data[i]?.id,
                                            name = "" + response.data.data[i]?.name,
                                            regex = "" + response.data.data[i]?.regex,
                                            viewbill = "" + response.data.data[i]?.viewbill
                                        )
                                    )
                                }

                                val categoryOpNameAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    categoryOpName
                                )
                                autoCompleteOperator.setAdapter(categoryOpNameAdapter)
                                autoCompleteOperator.onItemClickListener =
                                    AdapterView.OnItemClickListener { parent, view, position, id ->
                                        cardResult.visibility = View.GONE // hide previous results

                                        // todo : enable it if view bill is 0 in any case
//                                        if (selectedOpData[position].viewbill.equals("1")) {
                                        val opName = autoCompleteOperator.text.toString()
                                        Log.e(
                                            "getBbpsOperator ",
                                            "selectedOp : ${autoCompleteOperator.text}"
                                        )

                                        val indexOfName = categoryOpName.indexOf(opName)
                                        Log.e(
                                            "getBbpsOperator ",
                                            "indexOfName : $indexOfName // position : $position"
                                        )

                                        categoryId = categoryIdArr[indexOfName]
                                        opId = categoryIdArr[indexOfName]

                                        Log.e(
                                            "getBbpsOperator ",
                                            "opId : $opId // categoryId : $categoryId"
                                        )
                                        Log.e(
                                            "getBbpsOperator ",
                                            "selectedOpData1 : ${selectedOpData[position]}"
                                        )
                                        Log.e(
                                            "getBbpsOperator ",
                                            "selectedOpData2 : ${selectedOpData[indexOfName]}"
                                        )
//                                        Log.e("getBbpsOperator ", "selectedOpData2 : ${selectedOpData2.size} // selectedOpData : ${selectedOpData.size}")

                                        setupUi(indexOfName)

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
        viewModel.getLpgBbpsOperator(req)
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

                val displayName = selectedOpData[position].displayname.toUpperCase()
                with(displayName) {
                    selectedMode = when {
                        contains("NUMBER") -> "2"
                        contains("NO") -> "2"
                        contains("MOBILE NUMBER") -> "2"
                        else -> "3"
                    }
                }
            } else {
                etlBillNo.hint = "Bill number"
                etNumber.setText("")
                etlBillNo.visibility = View.VISIBLE
            }

            // ad1
            if (!selectedOpData[position].ad1_d_name.isNullOrEmpty() && !selectedOpData[position].ad1_d_name.equals(
                    "null"
                )
            ) {
                if (selectedOpData[position].ad1_d_name.equals("State", ignoreCase = true)) {
                    actlState.visibility = View.VISIBLE
                    selectedMode = "1"
                } else {
                    etlAd1.hint = selectedOpData[position].ad1_d_name
                    etlAd1.visibility = View.VISIBLE
                    actlState.visibility = View.GONE
                }
            } else {
                etAd1.setText("")
                etlAd1.visibility = View.GONE
                actlState.visibility = View.GONE
            }

            // ad2
            if (!selectedOpData[position].ad2_d_name.isNullOrEmpty() && !selectedOpData[position].ad2_d_name.equals(
                    "null"
                )
            ) {
                if (selectedOpData[position].ad2_d_name.equals("District")) {
                    actlDistrict.visibility = View.VISIBLE
                    selectedMode = "1"
                } else {
                    etlAd2.hint = selectedOpData[position].ad2_d_name
                    etlAd2.visibility = View.VISIBLE
                    actlDistrict.visibility = View.GONE
                }

            } else {
                etAd2.setText("")
                etlAd2.visibility = View.GONE
                actlDistrict.visibility = View.GONE
            }

            // ad3
            if (!selectedOpData[position].ad3_d_name.isNullOrEmpty() && !selectedOpData[position].ad3_d_name.equals(
                    "null"
                )
            ) {
                if (selectedOpData[position].ad3_d_name.equals("Distributor")) {
//                    actlDistributer.visibility = View.VISIBLE // removed dropdown
                    selectedMode = "1"
                    etlAd3.hint = selectedOpData[position].ad3_d_name + " ID"
//                    etAd3.inputType = InputType.TYPE_CLASS_NUMBER
                    etlAd3.visibility = View.VISIBLE
                    actlDistributer.visibility = View.GONE
                    btnFindDistributer.visibility = View.VISIBLE
                } else {
                    etlAd3.hint = selectedOpData[position].ad3_d_name
                    etlAd3.visibility = View.VISIBLE
                    actlDistributer.visibility = View.GONE
                    btnFindDistributer.visibility = View.INVISIBLE
                }
            } else {
                etAd3.setText("")
                etlAd3.visibility = View.GONE
                actlDistributer.visibility = View.GONE
                btnFindDistributer.visibility = View.INVISIBLE
            }
        }
    }

    private fun getPsFetchLpgOperator(req: GetPsFetchLpgBillReq) {
        viewModel.getPsFetchLpgOperator = MutableLiveData()
        viewModel.getPsFetchLpgOperator.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetPsFetchLpgBillRes::class.java)
                if (response != null) {
                    if (response.status) {
                        if (response.data.status) {
                            binding.apply {
                                cardResult.visibility = View.VISIBLE
                                tvUsername.text = "" + response?.data?.name
                                tvAmount.text = "" + response.data?.amount
                                etAmount.setText("" + response.data?.amount)
                                etlAmount.isEnabled = false
                            }
                        } else {
                            binding.apply {
                                cardResult.visibility = View.GONE
                                tvUsername.text = ""
                                tvAmount.text = ""
                                etAmount.setText("")
                                etlAmount.isEnabled = true
                            }
                            (activity as MainActivity).apiErrorDialog("" + response.data?.message)
                        }
                    } else {
                        binding.apply {
                            cardResult.visibility = View.GONE
                            tvUsername.text = ""
                            tvAmount.text = ""
                            etAmount.setText("")
                            etlAmount.isEnabled = true
                        }
                        (activity as MainActivity).apiErrorDialog("" + response?.data?.message)
                    }
                } else {
                    binding.apply {
                        cardResult.visibility = View.GONE
                        tvUsername.text = ""
                        tvAmount.text = ""
                        etAmount.setText("")
                        etlAmount.isEnabled = true
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    cardResult.visibility = View.GONE
                    tvUsername.text = ""
                    tvAmount.text = ""
                    etAmount.setText("")
                    etlAmount.isEnabled = true
                }
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getPsFetchLpgOperator(req)
    }

    private fun doPsLpgBillPay(req: GetPsLpgPayBillReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.doPsLpgBillPay = MutableLiveData()
        viewModel.doPsLpgBillPay.observe(requireActivity()) {
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
                            "LPG recharge successful of ${binding.autoCompleteOperator.text} for customer ${binding.etNumber.text}-${binding.etAd1.text}-${binding.etAd2.text}-${binding.etAd3.text} of amount ${binding.etAmount.text}"
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
        viewModel.doPsLpgBillPay(req)
    }

    private fun getLpgStateList(req: EmptyRequest) {
        viewModel.getLpgStateList = MutableLiveData()
        viewModel.getLpgStateList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetLpgStateListRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            val nameList = ArrayList<String>()
                            val idList = ArrayList<String>()
                            nameList.clear()
                            idList.clear()

                            for (i in response.data.indices) {
                                nameList.add("" + response.data[i]?.VALUE_DISPLAY)
                                idList.add("" + response.data[i]?.VALUE)
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                nameList
                            )
                            actState.setAdapter(adapter)
                            actState.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    actDistrict.setText("")
                                    selectedLpgState = idList[position]
                                    etAd1.setText("" + selectedLpgState)

                                    getLpgDistrictList(
                                        GetLpgDistrictReq(
                                            apiname = "GetLPGDistrictList",
                                            obj = Obj(StateId = "" + idList[position])
                                        )
                                    )
                                }
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
        viewModel.getLpgStateList(req)
    }

    private fun getLpgDistrictList(req: GetLpgDistrictReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getLpgDistrictList = MutableLiveData()
        viewModel.getLpgDistrictList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetLpgDistrictRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            LoadingUtils.hideDialog()
                            val nameList = ArrayList<String>()
                            val idList = ArrayList<String>()
                            val stateIdList = ArrayList<String>()
                            nameList.clear()
                            idList.clear()
                            stateIdList.clear()

                            for (i in response.data.indices) {
                                nameList.add("" + response.data[i]?.VALUE_DISPLAY)
                                idList.add("" + response.data[i]?.VALUE)
                                stateIdList.add("" + response.data[i]?.STATEID)
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                nameList
                            )
                            actDistrict.setAdapter(adapter)
                            actDistrict.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    selectedLpgDistrict = idList[position]
                                    etAd2.setText("" + selectedLpgDistrict)

//                                    getLpgDistributerList(
//                                        GetLpgDistrictReq(
//                                            apiname = "GetLPGDistributors",
//                                            obj = Obj(StateId = "" + stateIdList[position],
//                                                DistrictId = "" + selectedLpgDistrict
//                                            )
//                                        )
//                                    )
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
        viewModel.getLpgDistrictList(req)
    }

    private fun getLpgDistributerList(req: GetLpgDistrictReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getLpgDistrictList = MutableLiveData()
        viewModel.getLpgDistrictList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetLpgDistributerRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            val nameList = ArrayList<String>()
                            val idList = ArrayList<String>()
                            val stateIdList = ArrayList<String>()
                            nameList.clear()
                            idList.clear()
                            stateIdList.clear()

                            for (i in response.data.indices) {
                                nameList.add("" + response.data[i]?.VALUE_DISPLAY)
                                idList.add("" + response.data[i]?.VALUE)
                                stateIdList.add("" + response.data[i]?.STATE_ID)
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                nameList
                            )
                            actDistributer.setAdapter(adapter)
                            actDistributer.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    val indexOfName = nameList.indexOf("" + actDistributer.text)
                                    selectedLpgDistributer = idList[indexOfName]
                                    etAd3.setText("" + selectedLpgDistributer)
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
        viewModel.getLpgDistrictList(req)
    }
}