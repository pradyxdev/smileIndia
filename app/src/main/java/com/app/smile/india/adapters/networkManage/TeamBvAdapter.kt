/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.adapters.networkManage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsTeamBvBinding
import com.app.smile.india.models.networkManage.teamBv.Data


class TeamBvAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<TeamBvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsTeamBvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemsClick()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val binding: ItemsTeamBvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
                tvDate.text = "${response?.EntryDate.toString().ifEmpty { "N/A" }}"
                tvFromUserId.text = "${response?.FromUserId.toString().ifEmpty { "N/A" }}"
                tvLeftBv.text = "${response?.LeftBV.toString().ifEmpty { "N/A" }}"
                tvRightBv.text = "${response?.RightBV.toString().ifEmpty { "N/A" }}"
                tvPurType.text = "${response?.PurchaseType.toString().ifEmpty { "N/A" }}"
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
