/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.adapters.fundsManage

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsTransferFundsBinding
import com.app.smile.india.models.fundsManage.transferFundReport.Data
import kotlin.random.Random


class TransferFundsReportAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<TransferFundsReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsTransferFundsBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsTransferFundsBinding) :
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

                when (val walletType = response?.WalletType) {
                    "F" -> tvWallType.text = "Fund Wallet"
                    "P" -> tvWallType.text = "Payout Wallet"
                    "RC" -> tvWallType.text = "Recharge Wallet"
                    else -> tvWallType.text = "$walletType"
                }

                tvRechDate.text = "${response?.EntryDate.toString().ifEmpty { "N/A" }}"
                tvToUser.text = "${response?.UserName.toString().ifEmpty { "N/A" }}"
                tvToUserId.text = "${response?.ToUserId.toString().ifEmpty { "N/A" }}"
                tvFromName.text = "${response?.FUserName.toString().ifEmpty { "N/A" }}"
                tvFromId.text = "${response?.UserId.toString().ifEmpty { "N/A" }}"
                tvAmt.text = "\u20B9${response?.Amount.toString().ifEmpty { "0.0" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
