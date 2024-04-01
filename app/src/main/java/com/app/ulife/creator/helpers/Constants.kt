/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:40 PM
 *
 */

package com.app.ulife.creator.helpers

object Constants {
    // add api base url
    const val BaseUrl = "http://apiurl.com/api/"
    const val dummyQrCode =
        "https://firebasestorage.googleapis.com/v0/b/app-otp-xxxx.appspot.com/o/qrcode.png?alt=media&token=c4672ab7-29c9-4069-8f14-bf22d40a8651"
    const val dummyImg =
        "https://firebasestorage.googleapis.com/v0/b/app-otp-xxxx.appspot.com/o/dummy_user.png?alt=media&token=773d79d3-bb00-4e07-b0a0-9e3d0963ddcc"
    const val FirebaseDBUrl =
        "https://app-otp-xxxx-default-rtdb.asia-southeast1.firebasedatabase.app/"

    // dummy arrays
    val genderArray = arrayListOf("Male", "Female", "Other")

    var apiErrors = ""

    var defaultMapLoc =
        "2/501, Vijay Khand, Ujariyaon, Vijay Khand 2, Gomti Nagar, Lucknow, Uttar Pradesh 226010, India"

    var imgBaseUrl = "http://ramapi.sigmasoftwares.co/"

    const val userTypeCustomer = "customer"
    const val userTypeDepartment = "department"
    const val userTypeAdmin = "admin"

    val stateArray = arrayListOf(
        "Andhra Pradesh Telangana",
        "Assam",
        "Bihar Jharkhand",
        "Chennai",
        "Delhi NCR",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jammu Kashmir",
        "Karnataka",
        "Kerala",
        "Kolkata",
        "Madhya Pradesh Chhattisgarh",
        "Maharashtra Goa",
        "Mumbai",
        "North East",
        "Orissa",
        "Punjab",
        "Rajasthan",
        "Tamil Nadu",
        "UP East",
        "UP West",
        "West Bengal"
    )

//    val itemsGridSmall = listOf(
//        ItemsMD(R.drawable.s1, "Men's Shoes", "Example 1", "2599", ""),
//        ItemsMD(R.drawable.s2, "Men's Shoes", "Example 2", "3399", ""),
//        ItemsMD(R.drawable.s3, "Women's Shoes", "Example 3", "2299", ""),
//        ItemsMD(R.drawable.s4, "Men's Shoes", "Example 4", "6999", ""))
//
//    val itemsGridBig = listOf(
//        ItemsMD(R.drawable.s1, "Men's Shoes", "Example 1", "2599", ""),
//        ItemsMD(R.drawable.s2, "Men's Shoes", "Example 2", "3399", ""),
//        ItemsMD(R.drawable.s3, "Women's Shoes", "Example 3", "2299", ""),
//        ItemsMD(R.drawable.s4, "Men's Shoes", "Example 4", "6999", ""),
//        ItemsMD(R.drawable.s5, "Men's Shoes", "Example 5", "4599", ""),
//        ItemsMD(R.drawable.s6, "Men's Shoes", "Example 6", "2799", ""),
//
//        ItemsMD(R.drawable.s1, "Men's Shoes", "Example 1", "2599", ""),
//        ItemsMD(R.drawable.s2, "Men's Shoes", "Example 2", "3399", ""),
//        ItemsMD(R.drawable.s3, "Women's Shoes", "Example 3", "2299", ""),
//        ItemsMD(R.drawable.s4, "Men's Shoes", "Example 4", "6999", ""),
//        ItemsMD(R.drawable.s5, "Men's Shoes", "Example 5", "4599", ""),
//        ItemsMD(R.drawable.s6, "Men's Shoes", "Example 6", "2799", ""),
//
//        ItemsMD(R.drawable.s1, "Men's Shoes", "Example 1", "2599", ""),
//        ItemsMD(R.drawable.s2, "Men's Shoes", "Example 2", "3399", ""),
//        ItemsMD(R.drawable.s3, "Women's Shoes", "Example 3", "2299", ""),
//        ItemsMD(R.drawable.s4, "Men's Shoes", "Example 4", "6999", ""),
//        ItemsMD(R.drawable.s5, "Men's Shoes", "Example 5", "4599", ""),
//        ItemsMD(R.drawable.s6, "Men's Shoes", "Example 6", "2799", ""),
//    )

    val sizeArray = arrayListOf("5 UK", "6 UK", "7 UK", "8 UK", "9 UK")
    val sizeArray2 = arrayListOf("3", "4", "5", "6", "7", "8", "9", "10")

    val autoSizeUnitArr = arrayListOf("UK", "EU", "US")
    val autoSizeArr = arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    val autoGstSlabArr = arrayListOf("0%", "5%", "12%", "18%", "28%")
    val autoCatArr = arrayListOf("Adidas", "Nike", "Puma")

    val interests = arrayListOf(
        "\uD83D\uDC69 Wife",
        "\uD83D\uDE4E\u200D♂️ Husband",
        "\uD83D\uDC67 Girlfriend",
        "\uD83D\uDE4E\u200D♂️ Boyfriend",
        "\uD83D\uDC6D Friend",
        "\uD83D\uDC69 Mother",
        "\uD83D\uDC68 Father",
        "\uD83D\uDC67 Daughter",
        "\uD83E\uDDD1 Son",
        "\uD83D\uDC66 Brother",
        "\uD83D\uDC67 Sister",
        "\uD83D\uDC6D Cousin",
        "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66 Other",
    )

    var searchedLat = 0.0
    var searchedLog = 0.0
}