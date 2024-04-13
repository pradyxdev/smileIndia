/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.ulife.creator.adapters.epinManage

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creator.databinding.ItemsEpinTransferReportBinding
import com.app.ulife.creator.models.epinManage.epinTransferReport.Data
import kotlin.random.Random

class EpinTransferReportAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<EpinTransferReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsEpinTransferReportBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsEpinTransferReportBinding) :
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

                tvUsername.text = "${
                    response?.username.toString().ifEmpty { "N/A" }
                }, (${response?.UserId.toString().ifEmpty { "N/A" }})"
                tvDate.text = "${response?.EntryDate.toString().ifEmpty { "N/A" }}"
                tvAvailPin.text = "${response?.AvailablePin.toString().ifEmpty { "N/A" }}"
                tvNoPin.text = "${response?.NoOfPin.toString().ifEmpty { "N/A" }}"
//                tvPkgId.text = "${response?.PackageId.toString().ifEmpty { "N/A" }}"
                tvAmt.text = "\u20B9${response?.TotalAmount.toString().ifEmpty { "0.0" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
