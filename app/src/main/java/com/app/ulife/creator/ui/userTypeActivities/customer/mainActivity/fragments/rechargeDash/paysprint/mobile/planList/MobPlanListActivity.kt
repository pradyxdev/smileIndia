/*
 * *
 *  * Created by Prady on 4/20/24, 5:52 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 5:48 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList

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
import com.app.ulife.creator.helpers.Coroutines
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.paysprint.psMobPlanList.G4G
import com.app.ulife.creator.models.paysprint.psMobPlanList.GetPsMobPlanListReq
import com.app.ulife.creator.models.paysprint.psMobPlanList.GetPsMobPlanListRes
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.snackbar
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.delay
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MobPlanListActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var binding: ActivityPlanListBinding

    var type = ""
    var circle = ""
    var operator = ""
    var amount = ""

    //    var plansFullTTList = ArrayList<G4G>()
    var plansTopupList = ArrayList<G4G>()
    var plansG34List = ArrayList<G4G>()

    //    var plansG2List = ArrayList<G4G>()
//    var plansSmsList = ArrayList<G4G>()
    var plansComboList = ArrayList<G4G>()
    var plansRomaingList = ArrayList<G4G>()
    var plansRateCutter = ArrayList<G4G>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory)[SharedVM::class.java]
        preferenceManager = PreferenceManager.instance
        getIntentData()
        setupViews()
    }

    private fun getIntentData() {
        type = intent.getStringExtra("type").toString()
        circle = intent.getStringExtra("circle").toString()
        operator = intent.getStringExtra("operator").toString()
    }

    private fun setupViews() {
        getPsMobPlanList(
            GetPsMobPlanListReq(
                circle = "" + circle,
                op = "" + operator,
                userid = "" + preferenceManager.userid
            )
        )

        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
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

    private fun getPsMobPlanList(request: GetPsMobPlanListReq) {
        LoadingUtils.showDialog(this, false)
        viewModel.getPsMobPlanList = MutableLiveData()
        viewModel.getPsMobPlanList.observe(this) {
            try {
                val response = Gson().fromJson(it, GetPsMobPlanListRes::class.java)
//            Log.e("getMobPlans ", "$response")
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        plansG34List.clear()
                        if (!response.data.info.G34.isNullOrEmpty()) {
                            for (i in response.data.info.G34.indices) {
                                plansG34List.addAll(response.data.info.G34)
                            }
                        }
                        plansTopupList.clear()
                        if (!response.data.info.topup.isNullOrEmpty()) {
                            for (i in response.data.info.topup.indices) {
                                plansTopupList.addAll(response.data.info.topup)
                            }
                        }
                        plansComboList.clear()
                        if (!response.data.info.combo.isNullOrEmpty()) {
                            for (i in response.data.info.combo.indices) {
                                plansComboList.addAll(response.data.info.combo)
                            }
                        }
                        plansRateCutter.clear()
                        if (!response.data.info.rateCutter.isNullOrEmpty()) {
                            for (i in response.data.info.rateCutter.indices) {
                                plansRateCutter.addAll(response.data.info.rateCutter)
                            }
                        }
                        plansRomaingList.clear()
                        if (!response.data.info.romaing.isNullOrEmpty()) {
                            for (i in response.data.info.romaing.indices) {
                                plansRomaingList.addAll(response.data.info.romaing)
                            }
                        }

                        Coroutines.main {
                            delay(1000)
                            // create a list of tabs
                            createTabs()
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.root.snackbar("Sorry no plans found in this section !!", "s")
                    }

                } else {
                    LoadingUtils.hideDialog()
                    apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                if (it.contains("Plan Not Available", ignoreCase = true)) {
                    this.toast("Plan Not Available !")
                    finish()
                } else {
                    this.toast("$e\n$it")
//                apiErrorDialog("$e\n$it")
                    finish()
                }
            }
        }
        viewModel.getPsMobPlanList(request)
    }

    fun getPlanDetails(amt: String, desc: String) {
        amount = amt
        Log.e("amt_selected", amount)

        val intent = Intent()
        intent.putExtra("amount", amount)
        intent.putExtra("desc", desc)
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