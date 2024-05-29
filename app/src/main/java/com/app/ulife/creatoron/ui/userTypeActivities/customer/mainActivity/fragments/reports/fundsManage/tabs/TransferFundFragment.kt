/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.fundsManage.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creatoron.databinding.FragmentTransferFundBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.CommonUserIdReq
import com.app.ulife.creatoron.models.UserIdObj
import com.app.ulife.creatoron.models.fundsManage.transferFund.GetTransferFundsReq
import com.app.ulife.creatoron.models.fundsManage.transferFund.GetTransferFundsRes
import com.app.ulife.creatoron.models.userDetails.UserDetailsRes
import com.app.ulife.creatoron.models.wallet.WalletReq
import com.app.ulife.creatoron.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.afterTextChanged
import com.app.ulife.creatoron.utils.hideKeyboard
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class TransferFundFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentTransferFundBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    val p2pArray = arrayListOf("User")
    val walletTypeArray = arrayListOf(
//        "Payout to Fund Wallet",
        "Fund to Shopping Wallet", "Shopping to Shopping Wallet", "Payout To Recharge Wallet"
    )
    val walletTypeIdArray = arrayListOf(
//        "P",
        "F", "R", "RC"
    )
    val selectedWalletTypeIdArray = arrayListOf(
//        "P",
        "F", "R", "P"
    )
    var walletTypeId = ""
    var selectedWalletTypeId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferFundBinding.inflate(layoutInflater)
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
            tvToolbarTitle.text = "Transfer Funds"
            tvWalletAmt.visibility = View.VISIBLE

            val p2pAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, p2pArray
            )
            actP2pType.setAdapter(p2pAdapter)

            val walletAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1, walletTypeArray
            )
            actWallet.setAdapter(walletAdapter)
            actWallet.setOnItemClickListener { adapterView, view, i, l ->
                walletTypeId = walletTypeIdArray[i]
                println("$walletTypeId")
                etAmount.setText("")

                selectedWalletTypeId = selectedWalletTypeIdArray[i]

                getWalletBalance(
                    WalletReq(
                        apiname = "GetWalletBalance",
                        obj = com.app.ulife.creatoron.models.wallet.Obj(
                            datatype = "T",
                            transactiontype = "",
                            userid = "" + preferenceManager.userid,
                            wallettype = "" + selectedWalletTypeId
                        )
                    )
                )
            }
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

            etAmount.afterTextChanged {
                if (it.isNotEmpty()) {
                    if (etAmount.text.toString().toDouble() > tvWalletAmt.text.toString()
                            .toDouble()
                    ) {
                        etAmount.error = "Insufficient fund !"
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

            btnSubmit.onDebouncedListener {
                when {
                    actP2pType.text.toString().isEmpty() -> actP2pType.error =
                        "Please enter your p2p type"

                    actWallet.text.toString().isEmpty() -> actWallet.error =
                        "Please enter your wallet type"

                    etAmount.text.toString().isEmpty() -> etAmount.error =
                        "Please enter your amount"

                    etToUserid.text.toString().isEmpty() -> etToUserid.error =
                        "Please enter user id"

                    else -> {
                        startFundTransfer(
                            GetTransferFundsReq(
                                apiname = "TransferFundToUser",
                                obj = com.app.ulife.creatoron.models.fundsManage.transferFund.Obj(
                                    P2Ptype = "" + actP2pType.text,
                                    amount = "" + etAmount.text,
                                    touserid = "" + etToUserid.text,
                                    type = "" + walletTypeId,
                                    userid = "" + preferenceManager.userid
                                )
                            )
                        )
                    }
                }
            }
        }
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

    private fun getWalletBalance(req: WalletReq) {
        viewModel.getWalletBalance = MutableLiveData()
        viewModel.getWalletBalance.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetWalletBalRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            tvWalletAmt.text = "" + response.data[0]?.Balance
                            etlAmount.isEnabled = true
                        }
                    } else {
                        binding.apply {
                            tvWalletAmt.text = "0.0"
                            etlAmount.isEnabled = false
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        tvWalletAmt.text = "0.0"
                        etlAmount.isEnabled = false
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    tvWalletAmt.text = "0.0"
                    etlAmount.isEnabled = false
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }

    private fun startFundTransfer(req: GetTransferFundsReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.startFundTransfer = MutableLiveData()
        viewModel.startFundTransfer.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetTransferFundsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        if (!response.data.isNullOrEmpty()) {
                            when (response.data[0].id) {
                                1 -> (activity as MainActivity).apiSuccessDialog(successMsg = "Transfer successful of \u20B9${req.obj.amount}",
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
        viewModel.startFundTransfer(req)
    }
}