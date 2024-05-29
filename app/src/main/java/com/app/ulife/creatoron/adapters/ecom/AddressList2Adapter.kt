/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/5/24, 1:21 PM
 *
 */

package com.app.ulife.creatoron.adapters.ecom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creatoron.databinding.ItemsSavedAddress2Binding
import com.app.ulife.creatoron.models.shippingDetails.get.Data

class AddressList2Adapter(
    var context: Context,
    private val list: MutableList<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<AddressList2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsSavedAddress2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(response: Data)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
//        return 10
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val binding: ItemsSavedAddress2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
//                tvUsername.text = "Name : " + response.name
                tvMobile.text = "Name : " + response.FirstName + " (+91-" + response.Phone + ")"

                tvAddress.text =
                    "" + response.FullAddress + ", " + response.StateId + ", " + response.CityId + ", " + response.PinCode

                cardContainer.setOnClickListener {
                    listener.onItemClick(response)
                }
            }
        }
    }
}
