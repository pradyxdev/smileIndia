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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.ItemsAffiliatesBinding
import com.app.ulife.creatoron.models.affiliates.list.GetAffiliatesRes

class AffiliatesAdapter(
    var context: Context,
    private val list: List<GetAffiliatesRes.Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<AffiliatesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsAffiliatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(response: GetAffiliatesRes.Data)
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

    inner class ViewHolder(private val binding: ItemsAffiliatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: GetAffiliatesRes.Data, position: Int) {
            binding.apply {
                ivImg.load(response?.Logo) {
                    placeholder(R.drawable.logo)
                    error(R.drawable.logo)
                }
                tvName.text = "" + response?.Name

                root.setOnClickListener {
                    listener.onItemClick(response)
                }
            }
        }
    }
}
