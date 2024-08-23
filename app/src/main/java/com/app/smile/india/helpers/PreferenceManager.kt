/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */
package com.app.smile.india.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager private constructor(context: Context) {

    var sharedPreferences: SharedPreferences
    var context: Context
    var editor: SharedPreferences.Editor

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: PreferenceManager
            private set

        @JvmStatic
        fun init(context: Context) {
            instance = PreferenceManager(context)
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        this.context = context
        editor = sharedPreferences.edit()
    }

    fun clear() {
        editor.clear()
        editor.apply()
        editor.commit()
    }

    var userid: String?
        get() = sharedPreferences.getString("userid", "")
        set(userid) {
            editor.putString("userid", userid)
            editor.commit()
        }
    var userType: String?
        get() = sharedPreferences.getString("userType", "")
        set(userType) {
            editor.putString("userType", userType)
            editor.commit()
        }
    var userName: String?
        get() = sharedPreferences.getString("userName", "")
        set(userName) {
            editor.putString("userName", userName)
            editor.commit()
        }
    var phone: String?
        get() = sharedPreferences.getString("phone", "")
        set(phone) {
            editor.putString("phone", phone)
            editor.commit()
        }
    var email: String?
        get() = sharedPreferences.getString("email", "")
        set(email) {
            editor.putString("email", email)
            editor.commit()
        }
    var fcmToken: String?
        get() = sharedPreferences.getString("fcmToken", "")
        set(fcmToken) {
            editor.putString("fcmToken", fcmToken)
            editor.commit()
        }
    var tempToken: String?
        get() = sharedPreferences.getString("TempToken", "false")
        set(Token) {
            editor.putString("TempToken", Token)
            editor.commit()
        }
    var loggedIn: String?
        get() = sharedPreferences.getString("LoggedIn", "")
        set(Token) {
            editor.putString("LoggedIn", Token)
            editor.commit()
        }

    var latitude: String?
        get() = sharedPreferences.getString("latitude", "")
        set(lat) {
            editor.putString("latitude", lat)
            editor.commit()
        }

    var longitude: String?
        get() = sharedPreferences.getString("longitude", "")
        set(log) {
            editor.putString("longitude", log)
            editor.commit()
        }

    var gpsAddress: String?
        get() = sharedPreferences.getString("gpsAddress", "")
        set(gpsAdd) {
            editor.putString("gpsAddress", gpsAdd)
            editor.commit()
        }
    var address: String?
        get() = sharedPreferences.getString("address", "")
        set(address) {
            editor.putString("address", address)
            editor.commit()
        }
    var subLocality: String?
        get() = sharedPreferences.getString("subLocality", "")
        set(subLocality) {
            editor.putString("subLocality", subLocality)
            editor.commit()
        }
    var state: String?
        get() = sharedPreferences.getString("state", "")
        set(state) {
            editor.putString("state", state)
            editor.commit()
        }
    var city: String?
        get() = sharedPreferences.getString("city", "")
        set(city) {
            editor.putString("city", city)
            editor.commit()
        }
    var pincode: String?
        get() = sharedPreferences.getString("pincode", "")
        set(pincode) {
            editor.putString("pincode", pincode)
            editor.commit()
        }
    var profileImgUrl: String?
        get() = sharedPreferences.getString(
            "profileImgUrl",
            Constants.dummyImg
        )
        set(profileImgUrl) {
            editor.putString("profileImgUrl", profileImgUrl)
            editor.commit()
        }

    var languagePrefs: String?
        get() = sharedPreferences.getString(
            "languagePrefs",
            "en"
        )
        set(languagePrefs) {
            editor.putString("languagePrefs", languagePrefs)
            editor.commit()
        }

    var isNewUser: Boolean
        get() = sharedPreferences.getBoolean("isNewUser", false)
        set(isNewUser) {
            editor.putBoolean("isNewUser", isNewUser)
            editor.commit()
        }

    var referralId: String?
        get() = sharedPreferences.getString("referralId", "")
        set(referralId) {
            editor.putString("referralId", referralId)
            editor.commit()
        }

    var upiId: String?
        get() = sharedPreferences.getString("upiId", "")
        set(upiId) {
            editor.putString("upiId", upiId)
            editor.commit()
        }

    var upiQrCode: String?
        get() = sharedPreferences.getString("upiQrCode", "")
        set(upiQrCode) {
            editor.putString("upiQrCode", upiQrCode)
            editor.commit()
        }

    var dob: String?
        get() = sharedPreferences.getString("dob", "")
        set(dob) {
            editor.putString("dob", dob)
            editor.commit()
        }

    var isKycVerified: Boolean
        get() = sharedPreferences.getBoolean("isKycVerified", false)
        set(isKycVerified) {
            editor.putBoolean("isKycVerified", isKycVerified)
            editor.commit()
        }

    // wallets
    var walletDeposit: String?
        get() = sharedPreferences.getString("walletDeposit", "0.0")
        set(walletDeposit) {
            editor.putString("walletDeposit", walletDeposit)
            editor.commit()
        }
    var walletWinning: String?
        get() = sharedPreferences.getString("walletWinning", "0.0")
        set(walletWinning) {
            editor.putString("walletWinning", walletWinning)
            editor.commit()
        }
    var walletBonus: String?
        get() = sharedPreferences.getString("walletBonus", "0.0")
        set(walletBonus) {
            editor.putString("walletBonus", walletBonus)
            editor.commit()
        }
    var mapDraggedAddress: String?
        get() = sharedPreferences.getString("mapDraggedAddress", "")
        set(mapDraggedAddress) {
            editor.putString("mapDraggedAddress", mapDraggedAddress)
            editor.commit()
        }
    var session: String?
        get() = sharedPreferences.getString("session", "")
        set(session) {
            editor.putString("session", session)
            editor.commit()
        }
    var token: String?
        get() = sharedPreferences.getString("token", "")
        set(token) {
            editor.putString("token", token)
            editor.commit()
        }
}