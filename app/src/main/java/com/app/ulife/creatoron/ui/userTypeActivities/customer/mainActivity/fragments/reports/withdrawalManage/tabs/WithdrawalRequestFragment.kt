/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.withdrawalManage.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creatoron.databinding.FragmentWithdrawalRequestBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.CommonUserIdReq
import com.app.ulife.creatoron.models.EmptyRequest
import com.app.ulife.creatoron.models.Obj
import com.app.ulife.creatoron.models.UserIdObj
import com.app.ulife.creatoron.models.fundsManage.transferFund.GetTransferFundsRes
import com.app.ulife.creatoron.models.userDetails.UserDetailsRes
import com.app.ulife.creatoron.models.wallet.WalletReq
import com.app.ulife.creatoron.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creatoron.models.withdrawManage.deduction.GetDeductionRes
import com.app.ulife.creatoron.models.withdrawManage.withdrawalReq.StartWithdrawalReq
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.afterTextChanged
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.utils.toast
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class WithdrawalRequestFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentWithdrawalRequestBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var tdsChargePercentage = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawalRequestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        hitApis()
        listeners()
    }

    private fun setupViews() {
        binding.apply {
            tvToolbarTitle.text = "Withdrawal Request"
            tvWalletAmt.visibility = View.VISIBLE
        }
    }

    private fun hitApis() {
        getUserDetails(
            CommonUserIdReq(
                apiname = "GetUserDetail",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )

        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = com.app.ulife.creatoron.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "P"
                )
            )
        )

        getDeduction(
            EmptyRequest(
                apiname = "GetDeduction", obj = Obj()
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
                    if (etAmount.text.toString().toDouble() > tvWalletAmt.text.toString()
                            .toDouble()
                    ) {
                        etAmount.error = "Insufficient fund !"
                        btnSubmit.isEnabled = false
                    } else {
                        if (tdsChargePercentage != 0.0) {
                            val amount = etAmount.text.toString().toDouble()
                            val tdsCharge: Double = amount * (tdsChargePercentage / 100)

                            etTdsCharge.setText("" + tdsCharge)

                            val netAmt = amount - tdsCharge
                            etNetAmount.setText("" + netAmt)
                        } else {
                            etTdsCharge.setText("0")
                            etNetAmount.setText("" + etAmount.text)
                        }

                        btnSubmit.isEnabled = true
                        etAmount.error = null
                    }
                } else {
                    btnSubmit.isEnabled = false
                    etAmount.error = "Please enter your amount !"
                    etTdsCharge.setText("")
                    etNetAmount.setText("")
                }
            }

            btnSubmit.onDebouncedListener {
                when {
                    etAmount.text.toString().isEmpty() -> etAmount.error =
                        "Please enter your amount"

                    else -> startWithdrawal(
                        StartWithdrawalReq(
                            apiname = "InsertWithdrawlRequest",
                            obj = com.app.ulife.creatoron.models.withdrawManage.withdrawalReq.Obj(
                                AccHolderName = "" + etBankHolderName.text,
                                AccNo = "" + etBankAcNum.text,
                                BankName = "" + etBankName.text,
                                IFSC = "" + etBankIfsc.text,
                                ShoppingCharge = "0",
                                Tax = "" + etTdsCharge.text,
                                TransPswd = "" + etPassword.text,
                                admincharge = "0",
                                amount = "" + etAmount.text,
                                chainid = "0x38",
                                coinprice = "0",
                                dollerprice = "0",
                                netamount = "" + etNetAmount.text,
                                toaccount = "0",
                                transfercoin = "0",
                                userid = "" + preferenceManager.userid
                            )
                        )
                    )
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
                            binding.apply {
                                etBankName.setText("${response.data[0].BankName}")
                                etBankHolderName.setText("${response.data[0].AccHolderName}")
                                etBankAcNum.setText("${response.data[0].AccNo}")
                                etBankIfsc.setText("${response.data[0].IFSC}")
                            }
                        } else {
                            Navigation.findNavController(binding.root).popBackStack()
                            context?.toast("Unable to fetch bank information !")
                            Log.e("getUserDetErr ", "" + response)
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).apiErrorDialog(response.message)
                        Navigation.findNavController(binding.root).popBackStack()
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                    Log.e("getUserDetails ", "" + Constants.apiErrors)
                    Navigation.findNavController(binding.root).popBackStack()
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as MainActivity).apiErrorDialog("$it\n$e")
                Navigation.findNavController(binding.root).popBackStack()
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
                            etBalance.setText("" + response.data[0]?.Balance)
                            etlAmount.isEnabled = true
                        }
                    } else {
                        binding.apply {
                            tvWalletAmt.text = "0.0"
                            etBalance.setText("0.0")
                            etlAmount.isEnabled = false
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        tvWalletAmt.text = "0.0"
                        etBalance.setText("0.0")
                        etlAmount.isEnabled = false
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    tvWalletAmt.text = "0.0"
                    etBalance.setText("0.0")
                    etlAmount.isEnabled = false
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }

    private fun getDeduction(req: EmptyRequest) {
        viewModel.getDeduction = MutableLiveData()
        viewModel.getDeduction.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetDeductionRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            tdsChargePercentage = response.data[0]?.tdswithpan.toString().toDouble()
                            etlTdsCharge.hint = "TDS Charge(${response.data[0]?.tdswithpan}%)"
                        }
                    } else {
                        binding.apply {
                            tdsChargePercentage = 0.0
                            etlTdsCharge.hint = "TDS Charge(0%)"
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        tdsChargePercentage = 0.0
                        etlTdsCharge.hint = "TDS Charge(0%)"
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    tdsChargePercentage = 0.0
                    etlTdsCharge.hint = "TDS Charge(0%)"
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getDeduction(req)
    }

    private fun startWithdrawal(req: StartWithdrawalReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.startWithdrawal = MutableLiveData()
        viewModel.startWithdrawal.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetTransferFundsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        if (!response.data.isNullOrEmpty()) {
                            when (response.data[0].id) {
                                1 -> (activity as MainActivity).apiSuccessDialog(successMsg = "Withdrawal request successful of \u20B9${req.obj.amount}",
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
        viewModel.startWithdrawal(req)
    }
}