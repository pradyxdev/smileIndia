/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage.tabs.DirectTeamFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage.tabs.DownlineFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage.tabs.GenelogyFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage.tabs.LevelTeamFragment
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.networkManage.tabs.TeamBvFragment

class NetworkManageViewPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragments = arrayOf<Fragment>(
        //Initialize fragments views
        //Fragment views are initialized like any other fragment (Extending Fragment)
        //First fragment to be displayed within the pager tab number 1
        DirectTeamFragment(),
        DownlineFragment(),
        LevelTeamFragment(),
        GenelogyFragment(),
        TeamBvFragment()
    )

    val mFragmentNames = arrayOf(
        //Tabs names array
        "Direct Team", "Down Line", "Level Team", "Genelogy", "Team BV"
    )

    override fun getItemCount(): Int {
        return mFragments.size//Number of fragments displayed
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}