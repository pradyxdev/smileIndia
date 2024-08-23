/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 1:24 PM
 *
 */

package com.app.smile.india.adapters.ecom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsOrderHistoryBinding
import com.app.smile.india.models.getSaleReport.GetSaleReportRes

class OrderListAdapter(
    var context: Context,
    private val list: List<GetSaleReportRes.Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onOrderItemClick(position: GetSaleReportRes.Data, type: String)
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

    inner class ViewHolder(private val binding: ItemsOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: GetSaleReportRes.Data, position: Int) {
            binding.apply {
                tvBillDate.text = "" + response?.InvoiceDate
                tvTrackingId.text = "" + response?.InvoiceNo
                tvShipAmt.text = "\u20B9" + response?.TotalAmount
                tvTrackingStatus.text = "" + response?.Status

                btnView.setOnClickListener {
                    listener.onOrderItemClick(response, "details")
                }
                btnInvoice.setOnClickListener {
                    listener.onOrderItemClick(response, "pdf")
                }
            }
        }
    }
}
