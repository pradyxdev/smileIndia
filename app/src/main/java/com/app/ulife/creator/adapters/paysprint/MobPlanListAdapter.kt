/*
 * *
 *  * Created by Prady on 5/1/23, 12:26 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/3/23, 4:17 PM
 *
 */

package com.app.ulife.creator.adapters.paysprint

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creator.databinding.ItemsPlanListCommonBinding
import com.app.ulife.creator.models.paysprint.psMobPlanList.G4G

class MobPlanListAdapter(
    var context: Context,
    private val list: List<G4G>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<MobPlanListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsPlanListCommonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(rs: String, desc: String)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemsPlanListCommonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: G4G, position: Int) {
            binding.apply {
                tvDesc.text = response.desc
                tvLastUpdated.text = response.last_update
                tvAmount.text = response.rs
                tvValidity.text = response.validity

                root.setOnClickListener {
                    listener.onItemClick(response.rs, response.desc)
                }
            }
        }
    }
}
