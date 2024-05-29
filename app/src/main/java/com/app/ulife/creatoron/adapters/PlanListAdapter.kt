/*
 * *
 *  * Created by Prady on 5/1/23, 12:26 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/3/23, 4:17 PM
 *
 */

package com.app.ulife.creatoron.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creatoron.databinding.ItemsPlanListCommonBinding
import com.app.ulife.creatoron.models.bbpsRecharge.mobPlan.PDetail

class PlanListAdapter(
    var context: Context,
    private val list: List<PDetail>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<PlanListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsPlanListCommonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(rs: String, desc: String, typesId: Int)
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

    inner class ViewHolder(private val binding: ItemsPlanListCommonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: PDetail, position: Int) {
            binding.apply {
                tvDesc.text = response.desc
//                tvLastUpdated.text = response.last_update
                tvLastUpdated.visibility = View.GONE
                tvAmount.text = response.rs
                tvValidity.text = response.validity

                root.setOnClickListener {
                    listener.onItemClick(response.rs, response.desc, response.types_Id)
                }
            }
        }
    }
}
