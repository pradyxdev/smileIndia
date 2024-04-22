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
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.paysprint.psMobPlanList.G4G
import com.app.ulife.creator.models.paysprint.psMobPlanList.GetPsMobPlanListReq
import com.app.ulife.creator.models.paysprint.psMobPlanList.GetPsMobPlanListRes
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.snackbar
import com.app.ulife.creator.viewModels.SharedVM
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
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

    var plansFullTTList = ArrayList<G4G>()
    var plansTopupList = ArrayList<G4G>()
    var plansG34List = ArrayList<G4G>()
    var plansG2List = ArrayList<G4G>()
    var plansSmsList = ArrayList<G4G>()
    var plansComboList = ArrayList<G4G>()
    var plansRomaingList = ArrayList<G4G>()

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

                        plansFullTTList.clear()
                        plansTopupList.clear()
                        plansG34List.clear()
                        plansG2List.clear()
                        plansSmsList.clear()
                        plansComboList.clear()
                        plansRomaingList.clear()

                        if (!response.data.info.FULLTT.isNullOrEmpty()) {
                            for (i in response.data.info.FULLTT.indices) {
                                plansFullTTList.add(
                                    G4G(
                                        response.data.info.FULLTT[i].desc,
                                        response.data.info.FULLTT[i].last_update,
                                        response.data.info.FULLTT[i].rs,
                                        response.data.info.FULLTT[i].validity
                                    )
                                )
                            }
                            Log.e("plansTList ", "" + plansFullTTList)
                        }

                        if (!response.data.info.TOPUP.isNullOrEmpty()) {
                            for (i in response.data.info.TOPUP.indices) {
                                plansTopupList.add(
                                    G4G(
                                        response.data.info.TOPUP[i].desc,
                                        response.data.info.TOPUP[i].last_update,
                                        response.data.info.TOPUP[i].rs,
                                        response.data.info.TOPUP[i].validity
                                    )
                                )
                            }
                        }

                        if (!response.data.info.G23.isNullOrEmpty()) {
                            for (i in response.data.info.G23.indices) {
                                plansG34List.add(
                                    G4G(
                                        response.data.info.G23[i].desc,
                                        response.data.info.G23[i].last_update,
                                        response.data.info.G23[i].rs,
                                        response.data.info.G23[i].validity
                                    )
                                )
                            }
                        }

                        if (!response.data.info.G2.isNullOrEmpty()) {
                            for (i in response.data.info.G2.indices) {
                                plansG2List.add(
                                    G4G(
                                        response.data.info.G2[i].desc,
                                        response.data.info.G2[i].last_update,
                                        response.data.info.G2[i].rs,
                                        response.data.info.G2[i].validity
                                    )
                                )
                            }
                        }

                        if (!response.data.info.SMS.isNullOrEmpty()) {
                            for (i in response.data.info.SMS.indices) {
                                plansSmsList.add(
                                    G4G(
                                        response.data.info.SMS[i].desc,
                                        response.data.info.SMS[i].last_update,
                                        response.data.info.SMS[i].rs,
                                        response.data.info.SMS[i].validity
                                    )
                                )
                            }
                        }

                        if (!response.data.info.COMBO.isNullOrEmpty()) {
                            for (i in response.data.info.COMBO.indices) {
                                plansComboList.add(
                                    G4G(
                                        response.data.info.COMBO[i].desc,
                                        response.data.info.COMBO[i].last_update,
                                        response.data.info.COMBO[i].rs,
                                        response.data.info.COMBO[i].validity
                                    )
                                )
                            }
                        }

                        if (!response.data.info.Romaing.isNullOrEmpty()) {
                            for (i in response.data.info.Romaing.indices) {
                                plansRomaingList.add(
                                    G4G(
                                        response.data.info.Romaing[i].desc,
                                        response.data.info.Romaing[i].last_update,
                                        response.data.info.Romaing[i].rs,
                                        response.data.info.Romaing[i].validity
                                    )
                                )
                            }

                            // create a list of tabs
                            createTabs()
                        } else {
                            LoadingUtils.hideDialog()
                            binding.root.snackbar("Sorry no plans found in this section !!", "s")
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
                apiErrorDialog("$e\n$it")
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