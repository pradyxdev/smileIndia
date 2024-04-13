/*
 * *
 *  * Created by Prady on 03/03/23, 1:19 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 03/03/23, 1:19 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ActivityPlanListBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.bbpsRecharge.mobPlan.GetPlanListReq
import com.app.ulife.creator.models.bbpsRecharge.mobPlan.GetPlanListRes
import com.app.ulife.creator.models.bbpsRecharge.mobPlan.PDetail
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class PlanListActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var binding: ActivityPlanListBinding

    private var type = ""
    private var circle = ""
    private var operator = ""
    private var amount = ""

    var plansList = ArrayList<PDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory)[SharedVM::class.java]
        preferenceManager = PreferenceManager.instance
        getIntentData()
    }

    private fun getIntentData() {
        type = intent.getStringExtra("type").toString()
        circle = intent.getStringExtra("circle").toString()
        operator = intent.getStringExtra("operator").toString()

        if (circle.isNullOrEmpty() && operator.isNullOrEmpty()) {
            this.toast("No plans found for this circle !")
            finish()
        } else {
            setupViews()
        }
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
        getPlansList(
            GetPlanListReq(
                circlecode = "$circle",
                spkey = "$operator"
            )
        ) // circle, operator
    }

    private fun createTabs() {
        binding.myPager2.adapter = PlanListViewPager(this)
        TabLayoutMediator(
            binding.tabLayout, binding.myPager2
        ) { tab, position ->
            tab.text =
                (binding.myPager2.adapter as PlanListViewPager).mFragmentNames[position]
            //Sets tabs names as mentioned in ViewPagerAdapter fragmentNames array, this can be implemented in many different ways.
        }.attach()
    }

    private fun getPlansList(request: GetPlanListReq) {
        LoadingUtils.showDialog(this, false)
        viewModel.getPlansList = MutableLiveData()
        viewModel.getPlansList.observe(this) {
            val response = Gson().fromJson(it, GetPlanListRes::class.java)
//            Log.e("getMobPlans ", "$response")
            if (response != null) {
                if (response.status) {
                    LoadingUtils.hideDialog()
                    plansList.clear()

                    if (!response.data.pDetails.isNullOrEmpty()) {
                        for (i in response.data.types.indices) {
                            // storing plan types id
                            when (response.data.types[i].pType.toUpperCase()) {
                                "UNLIMITED" -> Constants.planTypeUnlimited =
                                    response.data.types[i].types_Id

                                "ISD" -> Constants.planTypeISD = response.data.types[i].types_Id
                                "DATA" -> Constants.planTypeData = response.data.types[i].types_Id
                                "INTERNATIONAL ROAMING" -> Constants.planTypeInternational =
                                    response.data.types[i].types_Id

                                "TALKTIME PLANS" -> Constants.planTypeTTPlan =
                                    response.data.types[i].types_Id

                                "INFLIGHT ROAMING PACKS" -> Constants.planTypeInflight =
                                    response.data.types[i].types_Id
                            }
                        }

                        plansList.addAll(response.data.pDetails)

                        createTabs()
                    } else {
                        LoadingUtils.hideDialog()
                        this.toast("Sorry no plans found in this section !")
                        finish()
                    }
                } else {
                    LoadingUtils.hideDialog()
                    this.toast("Sorry no plans found in this section !")
                    finish()
                }
            } else {
                LoadingUtils.hideDialog()
//                apiErrorDialog(Constants.apiErrors)
                this.toast(Constants.apiErrors)
            }
        }
        viewModel.getPlansList(request)
    }

    fun getPlanDetails(amt: String, desc: String, typesId: Int) {
        amount = amt
        Log.e("amt_selected", amount)

        val intent = Intent()
        intent.putExtra("amount", amount)
        intent.putExtra("desc", desc)
        intent.putExtra("typesId", typesId)
        setResult(2, intent)
        finish()
    }

    fun apiErrorDialog(errMsg: String) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_header_api_failed)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val title = dialog.findViewById<TextView>(R.id.tv_title)

        val subtitle = dialog.findViewById<TextView>(R.id.tv_sub_title)
        subtitle.text = errMsg

        val btnClose = dialog.findViewById<TextView>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.attributes = lp
    }
}