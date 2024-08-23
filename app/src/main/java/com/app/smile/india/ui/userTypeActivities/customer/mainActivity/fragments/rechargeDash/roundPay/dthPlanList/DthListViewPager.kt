/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList.tabs.AddOnFragment
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList.tabs.LanguagesFragment
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList.tabs.PlanFragment

class DthListViewPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragments = arrayOf<Fragment>(
        //Initialize fragments views
        //Fragment views are initialized like any other fragment (Extending Fragment)
        //First fragment to be displayed within the pager tab number 1
        PlanFragment(),
        AddOnFragment(),
        LanguagesFragment()
    )

    val mFragmentNames = arrayOf(
        //Tabs names array
        "All Plans",
        "Add-On",
        "Languages"
    )

    override fun getItemCount(): Int {
        return mFragments.size//Number of fragments displayed
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}