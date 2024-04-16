/*
 * *
 *  * Created by Prady on 4/13/24, 5:16 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 5:16 PM
 *
 */

package com.app.ulife.creator.models.fetchBill

data class Data(
    val hasErrors: Boolean,
    val itemArray: List<String>,
    val rowError: String,
    val rowState: Int,
    val table: List<Table>
)