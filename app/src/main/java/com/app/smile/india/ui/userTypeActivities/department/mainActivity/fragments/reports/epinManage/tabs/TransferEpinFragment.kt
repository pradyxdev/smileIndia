/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.reports.epinManage.tabs

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
import com.app.smile.india.databinding.FragmentEpinTransferBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.CommonUserIdReq
import com.app.smile.india.models.EmptyRequest
import com.app.smile.india.models.UserIdObj
import com.app.smile.india.models.epinManage.epinTopup.EpinTopupRes
import com.app.smile.india.models.epinManage.epinTransfer.EpinTransferReq
import com.app.smile.india.models.epinManage.epinTransferReport.EpinTransferReportReq
import com.app.smile.india.models.epinManage.epinTransferReport.EpinTransferReportRes
import com.app.smile.india.models.epinManage.planList.GetPlanRes
import com.app.smile.india.models.userDetails.UserDetailsRes
import com.app.smile.india.models.wallet.Obj
import com.app.smile.india.models.wallet.WalletReq
import com.app.smile.india.models.wallet.balance.GetWalletBalRes
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.afterTextChanged
import com.app.smile.india.utils.hideKeyboard
import com.app.smile.india.utils.onDebouncedListener
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class TransferEpinFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEpinTransferBinding
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
        binding = FragmentEpinTransferBinding.inflate(layoutInflater)
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
            tvToolbarTitle.text = "Transfer E-PIN"

            getPlanList(
                EmptyRequest(
                    apiname = "GetPlan", obj = com.app.smile.india.models.Obj(),
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

            etNumEpin.afterTextChanged {
                try {
                    if (etNumEpin.text.toString().toInt() <= etAvailEpin.text.toString().toInt()) {
                        val pkgAmt = etPkgAmt.text.toString().toDouble()
                        val myNumGen = etNumEpin.text.toString().toDouble()
                        val totAmt = pkgAmt * myNumGen
                        etTotAmt.setText("$totAmt")
                        etNumEpin.error = null

                        if (totAmt <= walletAmount) {
                            btnSubmit.isEnabled = true
                            etTotAmt.error = null
                        } else {
                            btnSubmit.isEnabled = false
                            etTotAmt.error = "Insufficient funds !"
                            context?.toast("Insufficient funds !")
                        }
                    } else {
                        etNumEpin.error = "E-Pin can't be greater than available E-Pins."
                        etTotAmt.setText("")
                        btnSubmit.isEnabled = false
                    }
                } catch (e: Exception) {
                    Log.e("etNumEpin ", "$e")
                    etTotAmt.setText("")
                    btnSubmit.isEnabled = false
                }
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

                    etNumEpin.text.toString().isEmpty() -> etNumEpin.error =
                        "Please enter num of e-pin"

                    else -> epinTransfer(
                        EpinTransferReq(
                            apiname = "TransferEpinByUser",
                            obj = com.app.smile.india.models.epinManage.epinTransfer.Obj(
                                entryby = "" + preferenceManager.userid,
                                noofpin = "" + etNumEpin.text,
                                planid = "" + pkgId,
                                userid = "" + preferenceManager.userid
                            )
                        )
                    )
                }
            }
        }
    }

    private fun epinTransfer(req: EpinTransferReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.epinTransfer = MutableLiveData()
        viewModel.epinTransfer.observe(requireActivity()) {
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.epinTransfer(req)
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
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
                                getTransferPinReport(
                                    EpinTransferReportReq(
                                        apiname = "GetEPINTransferReport",
                                        obj = com.app.smile.india.models.epinManage.epinTransferReport.Obj(
                                            from = "",
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.emptyRequestCommon(req)
    }

    private fun getTransferPinReport(req: EpinTransferReportReq) {
        viewModel.getTransferPinReport = MutableLiveData()
        viewModel.getTransferPinReport.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, EpinTransferReportRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.etAvailEpin.setText("" + response.data[0]?.AvailablePin)
                    } else {
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getTransferPinReport(req)
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getWalletBalance(req)
    }
}