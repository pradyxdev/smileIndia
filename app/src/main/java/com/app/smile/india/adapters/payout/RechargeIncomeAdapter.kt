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
import com.app.smile.india.databinding.ItemsRechargeIncomeBinding
import com.app.smile.india.models.payoutManage.rechargeIncome.Data
import kotlin.random.Random

class RechargeIncomeAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<RechargeIncomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsRechargeIncomeBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsRechargeIncomeBinding) :
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
                    response?.fromuserid.toString().ifEmpty { "N/A" }
                } (${response?.fromuserid.toString().ifEmpty { "N/A" }})"
                tvDate.text = "${response?.entrydate.toString().ifEmpty { "N/A" }}"
                tvType.text = "${response?.Remark.toString().ifEmpty { "N/A" }}"
                tvDirectIncome.text = "${response?.directincome.toString().ifEmpty { "N/A" }}"
                tvAdminCharge.text = "${response?.admincharge.toString().ifEmpty { "N/A" }}"
                tvTdsCharge.text = "${response?.tdscharge.toString().ifEmpty { "N/A" }}"
                tvPaybleAmount.text = "${response?.paybleamount.toString().ifEmpty { "N/A" }}"
                tvLevel.text = "${response?.Lvl.toString().ifEmpty { "N/A" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
