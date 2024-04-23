/*
 * *
 *  * Created by Prady on 8/3/23, 11:19 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 8/3/23, 11:14 AM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.reports.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creator.adapters.WalletListAdapter
import com.app.ulife.creator.adapters.rechargeHist.MobRechargeAdapter
import com.app.ulife.creator.databinding.FragmentRechargeHistoryBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.paysprint.history.mobHistory.GetMobRechargeHistReq
import com.app.ulife.creator.models.paysprint.history.mobHistory.GetMobRechargeHistRes
import com.app.ulife.creator.models.wallet.Obj
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.models.wallet.history.GetWalletHistoryRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class WalletHistoryFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentRechargeHistoryBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var fundType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRechargeHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        fundType = "" + arguments?.getString("fundType")

        binding.apply {
            tvWalletAmt.visibility = View.VISIBLE

            when (fundType) {
                "F" -> {
                    tvToolbarTitle.text = "Fund Wallet History"
                    hitApis()
                }

                "RC" -> {
                    tvToolbarTitle.text = "Recharge History"
                    containerChipFilter.visibility = View.VISIBLE
                    hitApis()
                }

                else -> {
                    context?.toast("Fund type not found !")
                    containerEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun hitApis() {
        // wallet amt
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = fundType
                )
            )
        )

        getWalletHistory(
            WalletReq(
                apiname = "GetWalletBalance",
                obj = Obj(
                    datatype = "H",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = fundType
                )
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

            chipAll.setOnClickListener {
                hitApis()
            }

            chipPrepaid.setOnClickListener {
                getMobRechargeHistory(
                    GetMobRechargeHistReq(
                        apiname = "PPRechargeHistory",
                        obj = com.app.ulife.creator.models.paysprint.history.mobHistory.Obj(
                            from = "",
                            to = "",
                            userid = "" + preferenceManager.userid
                        )
                    )
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

    private fun getWalletHistory(req: WalletReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getWalletHistory = MutableLiveData()
        viewModel.getWalletHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetWalletHistoryRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.GONE
                            rvList.visibility = View.VISIBLE
                            rvList.apply {
                                adapter = WalletListAdapter(context, response.data)
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.apply {
                        containerEmpty.visibility = View.VISIBLE
                        rvList.visibility = View.GONE
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.apply {
                    containerEmpty.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletHistory(req)
    }

    private fun getMobRechargeHistory(req: GetMobRechargeHistReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getMobRechargeHistory = MutableLiveData()
        viewModel.getMobRechargeHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetMobRechargeHistRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.GONE
                            rvList.visibility = View.VISIBLE
                            rvList.apply {
                                adapter = MobRechargeAdapter(context, response.data)
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.apply {
                        containerEmpty.visibility = View.VISIBLE
                        rvList.visibility = View.GONE
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.apply {
                    containerEmpty.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getMobRechargeHistory(req)
    }
}