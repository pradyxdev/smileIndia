/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.adapters.withdrawal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsWithdrawReqReportBinding
import com.app.smile.india.models.withdrawManage.withdrawReport.Data
import kotlin.random.Random

class WithdrawalReqReportAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<WithdrawalReqReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsWithdrawReqReportBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsWithdrawReqReportBinding) :
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

                tvRequestDate.text = "${response?.EntryDate.toString().ifEmpty { "N/A" }}"
                tvApprovalDate.text = "${response?.ApproveDate.toString().ifEmpty { "N/A" }}"
                tvBankName.text = "${response?.BankName.toString().ifEmpty { "N/A" }}"
                tvBankAcNum.text = "${response?.AccNo.toString().ifEmpty { "N/A" }}"
                tvReqAmt.text = "\u20B9${response?.Amount.toString().ifEmpty { "N/A" }}"
                tvTaxAmt.text = "\u20B9${response?.Tax.toString().ifEmpty { "N/A" }}"
                tvAmt.text = "\u20B9${response?.NetAmount.toString().ifEmpty { "N/A" }}"
                tvStatus.text = "Status : ${response?.Status.toString().ifEmpty { "N/A" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
