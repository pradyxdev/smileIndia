/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 25/02/23, 5:03 PM
 *
 */

package com.app.smile.india.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsRechargeDashboardBinding
import com.app.smile.india.models.dummys.CategoryMD
import kotlin.random.Random


class RechargeItemsAdapter(
    var context: Context,
    private val list: List<CategoryMD>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<RechargeItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsRechargeDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onRechargeItemsClick(name: String)
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

    inner class ViewHolder(private val binding: ItemsRechargeDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: CategoryMD, position: Int) {
            binding.apply {
                val color =
                    Color.argb(
                        225,
                        Random.nextInt(180),
                        Random.nextInt(180),
                        Random.nextInt(180)
                    ) // darker random color
                ivBg.setBackgroundColor(color)
                ivImg.setImageResource(response.img)
                ivImg.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)

                tvTitle.text = "" + response.name

                cardContainer.setOnClickListener {
                    listener.onRechargeItemsClick(response.name)
                }
            }
        }
    }
}
