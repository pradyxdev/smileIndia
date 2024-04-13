/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.DataFragment
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.ISDFragment
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.InflightFragment
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.InternationalFragment
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.TTPlanFragment
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs.UnlimitedFragment

class PlanListViewPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragments = arrayOf<Fragment>(
        //Initialize fragments views
        //Fragment views are initialized like any other fragment (Extending Fragment)
        //First fragment to be displayed within the pager tab number 1
        UnlimitedFragment(),
        DataFragment(),
        ISDFragment(),
        InternationalFragment(),
        TTPlanFragment(),
        InflightFragment()
    )

    val mFragmentNames = arrayOf(
        //Tabs names array
        "Unlimited",
        "Data",
        "ISD",
        "International Roaming",
        "Talktime Plans",
        "Inflight Roaming Pack"
    )

    override fun getItemCount(): Int {
        return mFragments.size//Number of fragments displayed
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}