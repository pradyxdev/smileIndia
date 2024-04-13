/*
 * *
 *  * Created by Prady on 4/4/24, 5:40 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 5:40 PM
 *
 */

package com.app.ulife.creator.models.shippingDetails.get

data class Data(
    val CityId: Int,
    val CompanyName: String,
    val Email: String,
    val EntryDate: String,
    val FirstName: String,
    val FullAddress: Any,
    val Id: Int,
    val LastName: String,
    val Phone: String,
    val PinCode: Int,
    val StateId: Int,
    val StreetAdd: String,
    val TownAdd: String,
    val UpdatedDate: String,
    val UserId: String
)