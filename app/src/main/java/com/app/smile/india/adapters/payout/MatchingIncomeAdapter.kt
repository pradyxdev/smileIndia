/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.adapters.payout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsMatchingIncomeBinding
import com.app.smile.india.models.payoutManage.matchingIncome.Data
import kotlin.random.Random

class MatchingIncomeAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<MatchingIncomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsMatchingIncomeBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsMatchingIncomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
                val color =
                    Color.argb(
                        225,
                        Random.nextInt(50),
                        Random.nextInt(50),
                        Random.nextInt(50)
                    ) // darker random color
                clContainer.setBackgroundColor(color)

//                ivImg.setImageResource(response.img)
//                ivImg.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)

                tvName.text = "${
                    response?.UserName.toString().ifEmpty { "N/A" }
                } (${response?.userid.toString().ifEmpty { "N/A" }})"
                tvFromDate.text = "${response?.FromDate.toString().ifEmpty { "N/A" }}"
                tvToDate.text = "${response?.ToDate.toString().ifEmpty { "N/A" }}"
                tvPair.text = "${response?.Pair.toString().ifEmpty { "N/A" }}"
                tvBinaryIncome.text = "${response?.BinaryIncome.toString().ifEmpty { "N/A" }}"
                tvTotLeft.text = "${response?.TotalLeft.toString().ifEmpty { "N/A" }}"
                tvTotRight.text = "${response?.TotalRight.toString().ifEmpty { "N/A" }}"
                tvLeftCarry.text = "${response?.LeftcarryPV.toString().ifEmpty { "N/A" }}"
                tvRightCarry.text = "${response?.RightCarryPv.toString().ifEmpty { "N/A" }}"
                tvUseLeft.text = "${response?.UseLeft.toString().ifEmpty { "N/A" }}"
                tvUseRight.text = "${response?.UseRight.toString().ifEmpty { "N/A" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
