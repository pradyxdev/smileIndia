/*
 * *
 *  * Created by Prady on 8/3/23, 11:19 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 8/3/23, 11:14 AM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.ledger

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creatoron.adapters.WalletListAdapter
import com.app.ulife.creatoron.databinding.FragmentLedgerBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.EmptyRequest
import com.app.ulife.creatoron.models.transactionType.TransactionTypeRes
import com.app.ulife.creatoron.models.wallet.Obj
import com.app.ulife.creatoron.models.wallet.WalletReq
import com.app.ulife.creatoron.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creatoron.models.wallet.history.GetWalletHistoryRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class LedgerFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentLedgerBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var transactionType = ""
    private var walletType = ""
    private var walletTypeName = listOf("Payout", "Fund", "Recharge")
    private var walletTypeId = listOf("P", "F", "RC")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLedgerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        walletType = "" + arguments?.getString("fundType")

        binding.apply {
            tvWalletAmt.visibility = View.VISIBLE

            val walletAdapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_list_item_1,
                walletTypeName
            )
            actWallet.setAdapter(walletAdapter)
            actWallet.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    walletType = walletTypeId[position]
                    actTransaction.setText("")
                    hitApis()
                }

            getTransactionType(
                EmptyRequest(
                    apiname = "GetTransactionType",
                    obj = com.app.ulife.creatoron.models.Obj()
                )
            )
        }
    }

    private fun hitApis() {
        // wallet amt
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = Obj(
                    datatype = "T",
                    transactiontype = "$transactionType",
                    userid = "" + preferenceManager.userid,
                    wallettype = walletType
                )
            )
        )

        getWalletHistory(
            WalletReq(
                apiname = "GetWalletBalance",
                obj = Obj(
                    datatype = "H",
                    transactiontype = "$transactionType",
                    userid = "" + preferenceManager.userid,
                    wallettype = walletType
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
                            if (!response.data.isNullOrEmpty()) {
                                containerEmpty.visibility = View.GONE
                                rvList.visibility = View.VISIBLE
                                rvList.apply {
                                    adapter = WalletListAdapter(context, response.data)
                                }
                            } else {
                                containerEmpty.visibility = View.VISIBLE
                                rvList.visibility = View.GONE
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

    private fun getTransactionType(req: EmptyRequest) {
        viewModel.getTransactionType = MutableLiveData()
        viewModel.getTransactionType.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, TransactionTypeRes::class.java)
                if (response != null) {
                    if (response.status) {
                        val listName = mutableListOf<String>()
                        listName.clear()

                        for (i in response.data.indices) {
                            listName.add(response.data[i].TransactionType)
                        }

                        binding.apply {
                            val adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.simple_list_item_1,
                                listName
                            )
                            actTransaction.setAdapter(adapter)
                            actTransaction.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    transactionType = listName[position]
                                    hitApis()
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
        viewModel.getTransactionType(req)
    }
}