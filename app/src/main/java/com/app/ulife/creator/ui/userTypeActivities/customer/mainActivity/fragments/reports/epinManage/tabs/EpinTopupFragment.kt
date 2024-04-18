/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.reports.epinManage.tabs

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
import com.app.ulife.creator.databinding.FragmentEpinTopupBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.CommonUserIdReq
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.UserIdObj
import com.app.ulife.creator.models.epinManage.epinReport.EPinReportReq
import com.app.ulife.creator.models.epinManage.epinReport.EPinReportRes
import com.app.ulife.creator.models.epinManage.epinTopup.EpinTopupReq
import com.app.ulife.creator.models.epinManage.epinTopup.EpinTopupRes
import com.app.ulife.creator.models.epinManage.planList.GetPlanRes
import com.app.ulife.creator.models.userDetails.UserDetailsRes
import com.app.ulife.creator.models.wallet.Obj
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.afterTextChanged
import com.app.ulife.creator.utils.hideKeyboard
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class EpinTopupFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEpinTopupBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var pkgId = ""
    private var epinTypeId = ""
    private var walletAmount = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpinTopupBinding.inflate(layoutInflater)
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
            tvToolbarTitle.text = "E-PIN Member Topup"

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

            btnVerifyMember.setOnClickListener {
                if (etToUserid.text.toString().isNotEmpty()) {
                    getUserDetails(
                        CommonUserIdReq(
                            apiname = "GetUserDetail",
                            obj = UserIdObj(userId = "" + etToUserid.text)
                        )
                    )
                } else {
                    etToUserid.error = "Enter Member ID first !"
                }
            }

            etToUserid.afterTextChanged {
                btnSubmit.isEnabled = false
                etUsername.setText("")
            }

            btnSubmit.onDebouncedListener {
                when {
                    etToUserid.text.toString().isEmpty() -> etToUserid.error =
                        "Please enter user id"

                    actPkg.text.toString().isEmpty() -> actPkg.error =
                        "Please select your package"

                    actEpinType.text.toString().isEmpty() -> actEpinType.error =
                        "Please select your epin type"

                    else -> epinTopup(
                        EpinTopupReq(
                            apiname = "TopupUserWithWallet",
                            obj = com.app.ulife.creator.models.epinManage.epinTopup.Obj(
                                epinid = "" + epinTypeId,
                                paymenttype = "EPin",
                                planid = "" + pkgId,
                                topupuserid = "" + etToUserid.text,
                                userid = "" + preferenceManager.userid
                            )
                        )
                    )
                }
            }
        }
    }

    private fun epinTopup(req: EpinTopupReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.epinTopup = MutableLiveData()
        viewModel.epinTopup.observe(requireActivity()) {
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
        viewModel.epinTopup(req)
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
                        if (!response.data.isNullOrEmpty()) {
                            binding.etToUserid.error = null
                            binding.btnSubmit.isEnabled = true
                            context?.hideKeyboard(binding.root)
//                            context?.toast("User found : ${response.data[0].UserName} (${mobileNoReq.obj.userId}")
                            binding.etUsername.setText("${response.data[0].UserName} (${mobileNoReq.obj.userId})")
                        } else {
                            Log.e("getUserDetails ", "" + response)
                            binding.etToUserid.error = "User not found !"
                            binding.btnSubmit.isEnabled = false
                            binding.etUsername.setText("")
                        }
                    } else {
                        LoadingUtils.hideDialog()
//                        (activity as MainActivity).apiErrorDialog(response.message)
                        Log.e("getUserDetails ", "" + response.message)
                        binding.etToUserid.error = "User not found !"
                        binding.btnSubmit.isEnabled = false
                        binding.etUsername.setText("")
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                    Log.e("getUserDetails ", "" + Constants.apiErrors)
                    binding.etToUserid.error = "User not found !"
                    binding.btnSubmit.isEnabled = false
                    binding.etUsername.setText("")
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$it\n$e")
                binding.etToUserid.error = "User not found !"
                binding.btnSubmit.isEnabled = false
            }
        }
        viewModel.getUserDetails(mobileNoReq)
    }

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

                        for (i in response.data.indices) {
                            listName.add("${response.data[i]?.PlanName}, (${response.data[i]?.Amount})")
                            listId.add("" + response.data[i]?.Id)
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
                                binding.actEpinType.setText("")

                                getEpinReport(
                                    EPinReportReq(
                                        apiname = "GetEPinReport",
                                        obj = com.app.ulife.creator.models.epinManage.epinReport.Obj(
                                            from = "",
                                            planid = "" + pkgId,
                                            status = "Unused",
                                            to = "",
                                            userid = "" + preferenceManager.userid
                                        )
                                    )
                                )
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

    private fun getEpinReport(req: EPinReportReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getEpinReport = MutableLiveData()
        viewModel.getEpinReport.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, EPinReportRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        val listName = mutableListOf<String>()
                        listName.clear()
                        val listId = mutableListOf<String>()
                        listId.clear()

                        if (!response.data.isNullOrEmpty()) {
                            for (i in response.data.indices) {
                                listName.add("${response.data[i]?.PinNumber}, (${response.data[i]?.PinAmount})")
                                listId.add("" + response.data[i]?.Id)
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                listName
                            )
                            binding.actEpinType.setAdapter(adapter)
                            binding.actEpinType.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    epinTypeId = listId[position]
                                    binding.btnSubmit.isEnabled = true
                                }
                        } else {
                            binding.btnSubmit.isEnabled = false
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
        viewModel.getEpinReport(req)
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
}