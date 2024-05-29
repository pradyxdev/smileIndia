/*
 * *
 *  * Created by Prady on 4/20/24, 5:52 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 5:48 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.ComboFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.FullTtFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.G34Fragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.RateCutFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.RomaingFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs.TopupFragment

class PlanListViewPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragments = arrayOf<Fragment>(
        //Initialize fragments views
        //Fragment views are initialized like any other fragment (Extending Fragment)
        //First fragment to be displayed within the pager tab number 1
        G34Fragment(),
        TopupFragment(),
        FullTtFragment(),
        ComboFragment(),
        RomaingFragment(),
        RateCutFragment(),
//        SmsFragment(),
//        G2Fragment(),
    )

    val mFragmentNames = arrayOf(
        //Tabs names array
        "3G/4G", "TOPUP", "Full Talk Time", "COMBO", "Roaming", "RATE CUTTER"
    )

    override fun getItemCount(): Int {
        return mFragments.size//Number of fragments displayed
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}