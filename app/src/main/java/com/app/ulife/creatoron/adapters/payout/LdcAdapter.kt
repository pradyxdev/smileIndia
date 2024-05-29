/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.ulife.creatoron.adapters.payout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creatoron.databinding.ItemsLdcBinding
import com.app.ulife.creatoron.models.payoutManage.ldc.report.Data
import kotlin.random.Random

class LdcAdapter(
    var context: Context,
    private val list: List<com.app.ulife.creatoron.models.payoutManage.ldc.report.Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<LdcAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsLdcBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsLdcBinding) :
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

                tvFromDate.text = "${response?.FromDate.toString().ifEmpty { "N/A" }}"
                tvToDate.text = "${response?.ToDate.toString().ifEmpty { "N/A" }}"
                tvBusiness.text = "${response?.Bussiness.toString().ifEmpty { "N/A" }}"
                tvPer.text = "${response?.Per.toString().ifEmpty { "N/A" }}"
                tvIncome.text = "${response?.Income.toString().ifEmpty { "N/A" }}"
                tvAdminCharge.text = "${response?.AdminCharge.toString().ifEmpty { "N/A" }}"
                tvTdsCharge.text = "${response?.TDSCharge.toString().ifEmpty { "N/A" }}"
                tvPayable.text = "${response?.Payable.toString().ifEmpty { "N/A" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
