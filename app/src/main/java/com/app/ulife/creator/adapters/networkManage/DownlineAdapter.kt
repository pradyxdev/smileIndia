/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.ulife.creator.adapters.networkManage

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creator.databinding.ItemsDownlineBinding
import com.app.ulife.creator.models.networkManage.downline.Data
import kotlin.random.Random

class DownlineAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<DownlineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsDownlineBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemsDownlineBinding) :
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

                tvName.text = "${response?.UserName} (${response?.UserId})"

                tvSponsorName.text = "${response?.SponsorName}"
                tvSponsorId.text = "${response?.SponsorId}"
                tvTopup.text = "${response?.TopupAmount.toString().ifEmpty { "N/A" }}"

                if (response.Status == 0) {
                    tvStatus.text = "De-Active"
                } else {
                    tvStatus.text = "Active"
                }

                tvDirection.text = "${response?.Direction}"
                tvRegDate.text = "${response?.RegDate.toString().ifEmpty { "N/A" }}"
                tvActDate.text = "${response?.ActivationDate.toString().ifEmpty { "N/A" }}"

//                cardContainer.strokeColor = color
//                cardContainer.setOnClickListener {
//                    listener.onDashboardItemsClick()
//                }
            }
        }
    }
}
