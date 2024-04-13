/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.ulife.creator.R
import com.app.ulife.creator.adapters.RechargeItemsAdapter
import com.app.ulife.creator.databinding.FragmentRechargeDashboardBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.wallet.Obj
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.calculateNoOfColumns
import com.app.ulife.creator.utils.getNavOptions
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class RechargeDashboardFragment : Fragment(), KodeinAware,
    RechargeItemsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentRechargeDashboardBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var slideFromRightAnimController: LayoutAnimationController
    private lateinit var fallDownAnimController: LayoutAnimationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRechargeDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupAnimations()
        setupViews()
        listeners()
    }

    private fun setupViews() {
        hitApis()
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
            llWallHistory.setOnClickListener {
                val args = Bundle()
                args.putString("fundType", "RC")
                findNavController().navigate(R.id.walletHistoryFragment, args, getNavOptions())
            }
            cardShopping.setOnClickListener {
                var url = "https://myshopprime.com/smart.creator./5vfcfab"
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://$url"
                    }
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                } catch (e: Exception) {
                    (activity as MainActivity).apiErrorDialog("$e")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = true)
    }

    private fun hitApis() {
        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "RC"
                )
            )
        )

        binding.rvList1.apply {
            val mNoOfColumns = context.calculateNoOfColumns(context, 120)
            val gridManager = GridLayoutManager(
                context,
                mNoOfColumns,
            )
            layoutManager = gridManager
            adapter = RechargeItemsAdapter(
                context,
                Constants.itemRecharge1,
                this@RechargeDashboardFragment
            )
            layoutAnimation = fallDownAnimController
        }

        binding.rvList2.apply {
            val mNoOfColumns = context.calculateNoOfColumns(context, 120)
            val gridManager = GridLayoutManager(
                context,
                mNoOfColumns,
            )
            layoutManager = gridManager
            adapter = RechargeItemsAdapter(
                context,
                Constants.itemRecharge2,
                this@RechargeDashboardFragment
            )
            layoutAnimation = fallDownAnimController
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
                            tvBalance.text = "" + response.data[0]?.Balance
                        }
                    } else {
                        binding.apply {
                            tvBalance.text = "0.0"
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        tvBalance.text = "0.0"
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    tvBalance.text = "0.0"
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }

    override fun onRechargeItemsClick(name: String) {
        when (name) {
            "Mobile Recharge" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Mobile Recharge")
                args.putString("rechargeType", "Prepaid")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "DTH Recharge" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "DTH Recharge")
                args.putString("rechargeType", "DTH")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Post-Paid Recharge" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Post-Paid Recharge")
                args.putString("rechargeType", "Postpaid")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Electricity" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Electricity Recharge")
                args.putString("rechargeType", "Electricity")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Broadband" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Broadband Recharge")
                args.putString("rechargeType", "Broadband")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Landline" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Landline Recharge")
                args.putString("rechargeType", "Landline")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Gas Line" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Gas Recharge")
                args.putString("rechargeType", "Gas")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Fastag" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Fastag Recharge")
                args.putString("rechargeType", "Fastag")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Cylinder" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Cylinder")
                args.putString("rechargeType", "Cylinder")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Water Bill" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Water Bill")
                args.putString("rechargeType", "Water")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            "Insurance" -> {
                val args = Bundle()
                args.putString("rechargeTypeName", "Insurance")
                args.putString("rechargeType", "Insurance")
                findNavController().navigate(R.id.rechargeCommonFragment, args, getNavOptions())
            }

            else -> context?.toast("Coming soon !")
        }
    }

    private fun setupAnimations() {
        slideFromRightAnimController = AnimationUtils.loadLayoutAnimation(
            requireActivity(),
            R.anim.ll_slide_in_from_right
        )
        fallDownAnimController =
            AnimationUtils.loadLayoutAnimation(
                requireActivity(),
                R.anim.ll_fall_down
            )
    }
}